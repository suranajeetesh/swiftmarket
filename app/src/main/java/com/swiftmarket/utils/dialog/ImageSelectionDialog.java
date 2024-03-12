package com.swiftmarket.utils.dialog;

import static com.swiftmarket.utils.ViewUtils.getCurrentTimestampAsString;
import static com.swiftmarket.utils.helper.PermissionHandler.requestCode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.swiftmarket.R;
import com.swiftmarket.utils.helper.PermissionHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Dialog for selecting images from the camera or gallery.
 * Created by Sam.
 */
public class ImageSelectionDialog {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int PICK_IMAGE_REQUEST_CODE=600;
    private FragmentActivity activity;
    private Fragment fragment;
    private ImageView view;
    private DialogClickListener dialogClickListener;

    public ImageSelectionDialog(Fragment fragment, ImageView view,DialogClickListener dialogClickListener){
        this.fragment=fragment;
        this.activity=fragment.requireActivity();
        this.view=view;
        this.dialogClickListener=dialogClickListener;
    }

    @SuppressLint({"QueryPermissionsNeeded", "IntentReset"})
    public void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
            if (dialogClickListener!=null) {
                switch (which) {
                    case 0 -> {
                        // Create an intent to capture a photo
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                            // Start the camera activity
                            fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        } else {
                            // Handle the case where no camera app is available
                            // You may want to show a message to the user
                            Toast.makeText(activity, "No camera app available", Toast.LENGTH_SHORT).show();
                        }
                    }
                    case 1 -> {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");

                        // Check if there's an activity to handle the intent
                        if (intent.resolveActivity(activity.getPackageManager()) != null) {
                            // Start the image picker activity
                            fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
                        } else {
                            // Handle the case where no activity can handle the intent
                            // You may want to show a message to the user
                            Toast.makeText(activity, "No app available to handle this action", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("Tag", "onActivityResult()--> requestCode" + requestCode);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Handle the selected image URI
            Uri selectedImageUri = data.getData();
            Glide.with(activity)
                    .load(selectedImageUri).centerCrop()
                    .error(R.drawable.ic_profile_holder)
                    .into(view);
            dialogClickListener.onCameraClick(selectedImageUri);
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            // Handle the captured image
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Glide.with(activity)
                    .load(imageBitmap).centerCrop()
                    .error(R.drawable.ic_profile_holder)
                    .into(view);
            File file = saveBitmapToInternalStorage(fragment.requireContext(), imageBitmap, "product_image_"+getCurrentTimestampAsString()+".png");
            dialogClickListener.onGalleryClick(Uri.fromFile(file));
        }
    }

    public void start(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions = new String[]{Manifest.permission.CAMERA};
        }
        if (!PermissionHandler.isPermissionsGranted(activity, permissions)) {
            PermissionHandler.requestPermissions(fragment, permissions, requestCode);
        } else {
            showImageSelectionDialog();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionHandler.requestCode) { // Use the same request code you used when requesting permissions
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                showImageSelectionDialog();
            } else {
                Toast.makeText(activity, activity.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static File saveBitmapToInternalStorage(Context context, Bitmap photo, String customName) {
        File file = new File(context.getCacheDir(), customName);
        try {
            file.delete(); // Delete the file, just in case there was still another file
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            fileOutputStream.write(byteArray);
            fileOutputStream.flush();
            fileOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


    public interface DialogClickListener{
        void onCameraClick(Uri selectedImageUri);
        void onGalleryClick(Uri uri);
    }
}

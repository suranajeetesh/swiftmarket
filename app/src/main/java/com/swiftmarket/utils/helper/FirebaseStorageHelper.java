package com.swiftmarket.utils.helper;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swiftmarket.utils.Constant;

import java.util.UUID;

/**
 * Created by Sam.
 */
public class FirebaseStorageHelper {
    private Context context;
    private FirebaseStorage storage;
    private String userUid;
    private StorageReference storageReference;
    private FirebaseStorageHelperInterface firebaseStorageHelperInterface;

    // Constructor
    public FirebaseStorageHelper(Context context) {
        this.context = context;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Preferences.getPreferences(context);
        userUid = Preferences.readString(context, Constant.KEY_USER_UID, "");
    }

    // Set the listener for Firebase Storage events
    public void setListener(FirebaseStorageHelperInterface firebaseStorageHelperInterface) {
        this.firebaseStorageHelperInterface = firebaseStorageHelperInterface;
    }

    // Upload a picture to Firebase Storage
    public void uploadPicture(Uri imageUri,String title,boolean isUserProfile) {
        final String randomKey;
        if (userUid == null) {
            randomKey = UUID.randomUUID().toString();
        } else {
            randomKey = userUid;
        }

        String productPath = "images/" + randomKey + "/product/" + title;
        String userProfilePath = "images/" + randomKey + "/profile/" + title;
        String path;
        if (isUserProfile) {
            path = userProfilePath;
        } else {
            path = productPath;
        }
        StorageReference riversRef = storageReference.child(path);
        riversRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            if (firebaseStorageHelperInterface != null) {
                riversRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                    String publicUrl = downloadUrl.toString();
                    firebaseStorageHelperInterface.onSuccess(taskSnapshot,publicUrl);
                });
            }
        }).addOnFailureListener(exception -> {
            if (firebaseStorageHelperInterface != null) {
                firebaseStorageHelperInterface.onError(exception.getMessage());
            }
        });
    }
    // Listener interface for Firebase Storage events
    public interface FirebaseStorageHelperInterface {
        // Method called on successful upload
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot, String publicUrl);

        // Method called on upload error
        public void onError(String message);

    }
}

package com.swiftmarket.ui.userAccess;

import static com.swiftmarket.utils.Constant.KEY_USER_PROFILE_URL;
import static com.swiftmarket.utils.ViewUtils.hide;
import static com.swiftmarket.utils.ViewUtils.show;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.UploadTask;
import com.swiftmarket.R;
import com.swiftmarket.databinding.FragmentSignupBinding;
import com.swiftmarket.ui.activity.MainActivity;
import com.swiftmarket.core.BaseFragment;
import com.swiftmarket.utils.Constant;
import com.swiftmarket.utils.EditTextValidator;
import com.swiftmarket.utils.helper.FirebaseAuthHelper;
import com.swiftmarket.utils.helper.FirebaseStorageHelper;
import com.swiftmarket.utils.dialog.ImageSelectionDialog;
import com.swiftmarket.utils.helper.MyFragmentUtil;
import com.swiftmarket.utils.helper.Preferences;
import com.swiftmarket.utils.helper.UserDatabaseHandler;


public class SignupFragment extends BaseFragment {

    FirebaseAuthHelper firebaseAuthHelper;
    private FragmentSignupBinding binding;
    private ImageSelectionDialog imageSelectionDialog;
    private FirebaseStorageHelper firebaseStorageHelper;
    private Uri uploadImageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseStorageHelper = new FirebaseStorageHelper(requireContext());
        Preferences.getPreferences(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        firebaseAuthHelper = new FirebaseAuthHelper(requireContext(), listener);
        return binding.getRoot();
    }


    FirebaseAuthHelper.FirebaseAuthHelperListener listener = new FirebaseAuthHelper.FirebaseAuthHelperListener() {
        @Override
        public void onError(String message) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
            hide(binding.includeLoader.getRoot());
        }

        @Override
        public void onSignUp() {
            if (getActivity() != null) {
                if (uploadImageUri!=null){
                    String UUID=Preferences.readString(requireContext(), Constant.KEY_USER_UID,"");
                    firebaseStorageHelper.uploadPicture(uploadImageUri,UUID,true);
                }else {
                    hide(binding.includeLoader.getRoot());
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();
        binding.btnSignup.setOnClickListener(v -> {
            MyFragmentUtil.hideKeyboard(requireActivity());
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();

            if (!EditTextValidator.isValidEmail(email)) {
                Snackbar snackbar = Snackbar.make(binding.clMain, R.string.invalid_email, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (!EditTextValidator.isTextFieldValid(password)) {
                Snackbar snackbar = Snackbar.make(binding.clMain, R.string.invalid_password, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                show(binding.includeLoader.getRoot());
                firebaseAuthHelper.onSingUp(email, password);
                binding.edtEmail.setText("");
                binding.edtPassword.setText("");
            }
        });
        binding.btnLogin.setOnClickListener(v -> MyFragmentUtil.goBackOnFirstFragment(requireActivity()));
        binding.btnBack.setOnClickListener(v -> MyFragmentUtil.goBack(requireActivity()));
        binding.profileImage.setOnClickListener(v -> {
            imageSelectionDialog.start();
        });

        firebaseStorageHelper.setListener(new FirebaseStorageHelper.FirebaseStorageHelperInterface() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot, String publicUrl) {
                hide(binding.includeLoader.getRoot());
                Preferences.writeString(requireActivity(),KEY_USER_PROFILE_URL,publicUrl);
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }

            @Override
            public void onError(String message) {
                hide(binding.includeLoader.getRoot());
                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDialog(){
        imageSelectionDialog = new ImageSelectionDialog(this, binding.profileImage, new ImageSelectionDialog.DialogClickListener() {
            @Override
            public void onCameraClick(Uri selectedImageUri) {
                uploadImageUri=selectedImageUri;
            }

            @Override
            public void onGalleryClick(Uri uri) {
                uploadImageUri=uri;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSelectionDialog.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageSelectionDialog.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
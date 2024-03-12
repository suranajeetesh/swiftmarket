package com.swiftmarket.ui.userAccess;

import static com.swiftmarket.utils.ViewUtils.hide;
import static com.swiftmarket.utils.ViewUtils.show;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.swiftmarket.R;
import com.swiftmarket.databinding.FragmentLoginBinding;
import com.swiftmarket.ui.activity.MainActivity;
import com.swiftmarket.core.BaseFragment;
import com.swiftmarket.utils.EditTextValidator;
import com.swiftmarket.utils.MoveUtils;
import com.swiftmarket.utils.helper.FirebaseAuthHelper;
import com.swiftmarket.utils.helper.MyFragmentUtil;

/**
 * Create by Sam.
 */

public class LoginFragment extends BaseFragment {
    FirebaseAuthHelper firebaseAuthHelper;
    SharedPreferences preferences;
    private FragmentLoginBinding binding;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        preferences = context.getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
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
        public void onLogin() {
            if (getActivity() != null) {
                hide(binding.includeLoader.getRoot());
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogin.setOnClickListener(v -> {
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
                firebaseAuthHelper.login(email, password);
                binding.edtEmail.setText("");
                binding.edtPassword.setText("");
            }
        });
        binding.btnSignup.setOnClickListener(v -> MoveUtils.moveSignUP(requireActivity()));
        binding.txtForgotPassword.setOnClickListener(v -> MoveUtils.moveForgotPassword(requireActivity()));
    }
}
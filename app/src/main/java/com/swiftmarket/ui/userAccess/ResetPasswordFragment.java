package com.swiftmarket.ui.userAccess;

import static com.swiftmarket.utils.helper.MyFragmentUtil.goBack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.swiftmarket.R;
import com.swiftmarket.databinding.FragmentResetPasswordBinding;
import com.swiftmarket.core.BaseFragment;
import com.swiftmarket.utils.helper.FirebaseAuthHelper;
import com.swiftmarket.utils.helper.MyFragmentUtil;
import com.swiftmarket.utils.EditTextValidator;

public class ResetPasswordFragment extends BaseFragment {
    FirebaseAuthHelper firebaseAuthHelper;
    FragmentResetPasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        firebaseAuthHelper = new FirebaseAuthHelper(requireContext(), listener);
        return binding.getRoot();
    }
    FirebaseAuthHelper.FirebaseAuthHelperListener listener = new FirebaseAuthHelper.FirebaseAuthHelperListener() {
        @Override
        public void onError(String message) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onForgotPassword() {
            goBack(requireActivity());
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnForgotPassword.setOnClickListener(v -> {
            MyFragmentUtil.hideKeyboard(requireActivity());
            String email = binding.edtEmail.getText().toString().trim();
            if (!EditTextValidator.isValidEmail(email)) {
                Snackbar snackbar = Snackbar.make(binding.clMain, R.string.invalid_email, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                firebaseAuthHelper.onForgotPassword(email);
                binding.edtEmail.setText("");
                Toast.makeText(requireActivity(), R.string.please_check_gmail_successful_sent_mail, Toast.LENGTH_LONG).show();
                MyFragmentUtil.addReplaceFragment((FragmentActivity) requireActivity(), R.id.fl_container, new LoginFragment(),
                        false, false);
            }
        });
        binding.btnBack.setOnClickListener(v -> MyFragmentUtil.addReplaceFragment((FragmentActivity) requireActivity(), R.id.fl_container, new LoginFragment(),
                false, false));
    }
}
package com.swiftmarket.ui.payment;

import static com.swiftmarket.utils.MoveUtils.moveSuccess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.swiftmarket.R;
import com.swiftmarket.app.App;
import com.swiftmarket.core.BaseFragment;
import com.swiftmarket.databinding.FragmentPaymentBinding;
import com.swiftmarket.ui.home.HomeViewModel;
import com.swiftmarket.utils.EditTextValidator;
import com.swiftmarket.utils.helper.MyFragmentUtil;


public class PaymentFragment extends BaseFragment {
    private FragmentPaymentBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = ((App) requireActivity().getApplication()).getHomeViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObserver();
        binding.btnBack.setOnClickListener(v -> MyFragmentUtil.goBack(requireActivity()));

        binding.btnProceed.setOnClickListener(v -> {
            String name = binding.edtHolderName.getText().toString().trim();
            String number = binding.edtCardNumber.getText().toString().trim();
            String cvv = binding.edtCvvCvc.getText().toString().trim();
            String date = binding.edtExpiryDate.getText().toString().trim();
            boolean validateTextField = EditTextValidator.areTextFieldsValid(name, number, cvv, date);
            if (!validateTextField) {
                Snackbar.make(binding.edtHolderName, getString(R.string.missing_field), Snackbar.LENGTH_LONG).show();
            } else {
                moveSuccess(requireActivity());
            }
        });
    }

    private void initObserver() {
        homeViewModel.getTotalAmount().observe(getViewLifecycleOwner(), totalAmount -> {
            if (!totalAmount.isEmpty()) {
                requireActivity().runOnUiThread(() -> binding.txtNumber.setText(requireContext().getResources().getString(R.string.dollar_value, totalAmount)));
            }
        });
    }
}
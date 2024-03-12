package com.swiftmarket.ui.success;

import static com.swiftmarket.utils.helper.MyFragmentUtil.goBackOnFirstFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.swiftmarket.R;
import com.swiftmarket.app.App;
import com.swiftmarket.core.BaseFragment;
import com.swiftmarket.databinding.FragmentPaymentBinding;
import com.swiftmarket.databinding.FragmentSuccessBinding;
import com.swiftmarket.ui.home.HomeViewModel;


public class SuccessFragment extends BaseFragment {
    private FragmentSuccessBinding binding;

    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = ((App) requireActivity().getApplication()).getHomeViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnBack.setOnClickListener(v -> {
            homeViewModel.clear();
            goBackOnFirstFragment(requireActivity());
        });

        binding.btnProceed.setOnClickListener(v -> {
            homeViewModel.clear();
            goBackOnFirstFragment(requireActivity());
        });
    }
}
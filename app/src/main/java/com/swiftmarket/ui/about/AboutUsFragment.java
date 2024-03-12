package com.swiftmarket.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swiftmarket.core.BaseFragment;
import com.swiftmarket.databinding.FragmentAboutUsBinding;
import com.swiftmarket.utils.helper.MyFragmentUtil;


public class AboutUsFragment extends BaseFragment {
    private FragmentAboutUsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnBack.setOnClickListener(v -> MyFragmentUtil.goBack(requireActivity()));
    }
}
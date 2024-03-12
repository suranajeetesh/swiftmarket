package com.swiftmarket.ui.product_details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.swiftmarket.R;
import com.swiftmarket.app.App;
import com.swiftmarket.data.LocalProductsModel;
import com.swiftmarket.databinding.FragmentProductDetailsBinding;
import com.swiftmarket.core.BaseFragment;
import com.swiftmarket.ui.home.HomeViewModel;
import com.swiftmarket.utils.MoveUtils;
import com.swiftmarket.utils.helper.FirebaseStorageHelper;
import com.swiftmarket.utils.helper.MyFragmentUtil;

public class ProductDetailsFragment extends BaseFragment {
    private FragmentProductDetailsBinding binding;

    private HomeViewModel homeViewModel;
    private int count = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = ((App) requireActivity().getApplication()).getHomeViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(requireContext())
                .load(homeViewModel.getSelectedData().getProductImage()).centerCrop()
                .error(R.drawable.ic_profile_holder)
                .into(binding.imgProductItem);
        binding.txtTitle.setText(homeViewModel.getSelectedData().getProductName());
        binding.txtProductDetails.setText(homeViewModel.getSelectedData().getProductDescription());
        binding.txtPrice.setText(getResources().getString(R.string.dollar_value, homeViewModel.getSelectedData().getProductPrice()));
        binding.btnBack.setOnClickListener(v -> MyFragmentUtil.goBack(requireActivity()));
        binding.includeButton.imgAddition.setOnClickListener(v -> {
            count += 1;
            int quantity = Integer.parseInt(homeViewModel.getSelectedData().getProductQuantity());
            if (count > quantity) {
                count = quantity;
            }
            binding.includeButton.itemNumber.setText(String.valueOf(count));
        });
        binding.includeButton.imgSubtraction.setOnClickListener(v -> {
            count -= 1;
            if (count < 1) {
                count = 1;
            }
            binding.includeButton.itemNumber.setText(String.valueOf(count));
        });

        binding.btnAdd.setOnClickListener(v -> {
            LocalProductsModel original = homeViewModel.getSelectedData();
            LocalProductsModel product = original.clone();
            product.setProductQuantity(String.valueOf(count));
            homeViewModel.addToCart(product);
            MoveUtils.moveAddToCart(requireActivity());
        });
    }
}
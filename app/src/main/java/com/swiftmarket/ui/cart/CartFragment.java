package com.swiftmarket.ui.cart;


import static com.swiftmarket.utils.MoveUtils.movePayment;
import static com.swiftmarket.utils.MoveUtils.moveProductDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.swiftmarket.R;
import com.swiftmarket.app.App;
import com.swiftmarket.data.LocalProductsModel;
import com.swiftmarket.databinding.FragmentCartBinding;
import com.swiftmarket.databinding.FragmentProductDetailsBinding;
import com.swiftmarket.ui.adapter.CartAdapter;
import com.swiftmarket.ui.adapter.ProductsAdapter;
import com.swiftmarket.ui.home.HomeViewModel;
import com.swiftmarket.utils.helper.MyFragmentUtil;

import java.util.ArrayList;


public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private CartAdapter cartAdapter;
    private final ArrayList<LocalProductsModel> productList = new ArrayList<>();

    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = ((App) requireActivity().getApplication()).getHomeViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObserver();
        initRecyclerView();
        productList.clear();
        productList.addAll(homeViewModel.getCartProductList());
        binding.btnBack.setOnClickListener(v -> MyFragmentUtil.goBack(requireActivity()));
        binding.btnProceed.setOnClickListener(v -> movePayment(requireActivity()));
    }

    private void initObserver(){
        homeViewModel.getTotalAmount().observe(getViewLifecycleOwner(), totalAmount -> {
            if (!totalAmount.isEmpty()){
                requireActivity().runOnUiThread(() -> binding.txtNumber.setText(requireContext().getResources().getString(R.string.dollar_value,totalAmount)));
            }
        });
    }

    private void initRecyclerView() {
        binding.cartList.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartAdapter = new CartAdapter(productList, new CartAdapter.ItemClickListener() {
            @Override
            public void itemClickIncrease(int position, LocalProductsModel productsModel) {

            }

            @Override
            public void itemClickDecrease(int position, LocalProductsModel productsModel) {

            }
        });
        binding.cartList.setAdapter(cartAdapter);
    }
}
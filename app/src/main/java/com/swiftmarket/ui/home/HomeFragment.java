package com.swiftmarket.ui.home;

import static com.swiftmarket.utils.MoveUtils.moveProductDetail;
import static com.swiftmarket.utils.MoveUtils.moveUpdateProduct;
import static com.swiftmarket.utils.ViewUtils.hide;
import static com.swiftmarket.utils.ViewUtils.show;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.swiftmarket.app.App;
import com.swiftmarket.core.BaseFragment;
import com.swiftmarket.data.LocalProductsModel;
import com.swiftmarket.databinding.FragmentHomeBinding;
import com.swiftmarket.ui.activity.MainActivity;
import com.swiftmarket.ui.adapter.ProductsAdapter;
import com.swiftmarket.utils.helper.DatabaseHandler;
import com.swiftmarket.utils.helper.FirebaseDBHelper;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;

    private HomeViewModel homeViewModel;

    private final ArrayList<LocalProductsModel> productList = new ArrayList<>();

    private FirebaseDBHelper firebaseDBHelper;
    private ProductsAdapter productsAdapter;

    DatabaseHandler db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDBHelper = FirebaseDBHelper.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ((App) requireActivity().getApplication()).getHomeViewModel();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        db = new DatabaseHandler(requireContext(), listener);
        return binding.getRoot();
    }

    DatabaseHandler.DatabaseHelperListener listener = new DatabaseHandler.DatabaseHelperListener() {
        @Override
        public void onError(String message) {

        }

        public void getAllRecords() {
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("Tag", "onClick--> getAllRecords" + new Gson().toJson(db.getAllRecords()));
        initRecyclerView();
        binding.imgHumberMenu.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).manageDrawerView(true);
        });
        firebaseDBHelper.setListener(new FirebaseDBHelper.FirebaseDBHelperInterface() {
            @Override
            public void readData(ArrayList<LocalProductsModel> productList) {
                HomeFragment.this.productList.clear();
                HomeFragment.this.productList.addAll(productList);
                setUI(productList.isEmpty());
                if (!productList.isEmpty()) {
                    productsAdapter.setEmployeeList(HomeFragment.this.productList);
                    productsAdapter.setOriginalProductList(HomeFragment.this.productList);
                    homeViewModel.setFullList(HomeFragment.this.productList);
                }
            }

            @Override
            public void onError(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        firebaseDBHelper.readData();

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                productsAdapter.getFilter().filter(s.toString());
                if (s.length() > 0) {
                    show(binding.imgClose);
                } else {
                    hide(binding.imgClose);
                }
            }
        });
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtSearch.setText("");
                hide(binding.imgClose);
            }
        });
    }

    private void initRecyclerView() {
        binding.productList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        productsAdapter = new ProductsAdapter(productList, new ProductsAdapter.ItemClickListener() {
            @Override
            public void itemClick(int position, LocalProductsModel productsModel) {
                homeViewModel.setSelectedIndex(position);
                homeViewModel.setSelectedData(productsModel);
                moveProductDetail(requireActivity());
            }

            @Override
            public void updateItemClick(int position, LocalProductsModel productsModel) {
                homeViewModel.setSelectedIndex(position);
                homeViewModel.setSelectedData(productsModel);
                moveUpdateProduct(requireActivity());
            }

            @Override
            public void onSearchResultsChanged(boolean isEmpty) {
                setUI(isEmpty);
            }
        });
        binding.productList.setAdapter(productsAdapter);
    }

    private void setUI(boolean isEmpty) {
        if (isEmpty) {
            hide(binding.productList);
            show(binding.txtNotDataFound);
        } else {
            show(binding.productList);
            hide(binding.txtNotDataFound);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
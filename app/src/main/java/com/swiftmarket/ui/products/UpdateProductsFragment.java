package com.swiftmarket.ui.products;

import static com.swiftmarket.utils.ViewUtils.hide;
import static com.swiftmarket.utils.ViewUtils.show;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.UploadTask;
import com.swiftmarket.R;
import com.swiftmarket.app.App;
import com.swiftmarket.data.LocalProductsModel;
import com.swiftmarket.databinding.FragmentUpdateProductsBinding;
import com.swiftmarket.ui.home.HomeViewModel;
import com.swiftmarket.utils.EditTextValidator;
import com.swiftmarket.utils.dialog.ConfirmationDialog;
import com.swiftmarket.utils.helper.DatabaseHandler;
import com.swiftmarket.utils.helper.FirebaseDBHelper;
import com.swiftmarket.utils.helper.FirebaseStorageHelper;
import com.swiftmarket.utils.dialog.ImageSelectionDialog;
import com.swiftmarket.utils.helper.LocationHelper;
import com.swiftmarket.utils.helper.MyFragmentUtil;

public class UpdateProductsFragment extends Fragment {
    private FragmentUpdateProductsBinding binding;
    private DatabaseHandler db;
    private ImageSelectionDialog imageSelectionDialog;
    private FirebaseStorageHelper firebaseStorageHelper;
    private FirebaseDBHelper firebaseDBHelper;
    private Uri uploadImageUri;
    private String selectedCategory;
    private String selectedImagePublicUrl;
    private LocationHelper locationHelper;

    private double latitude;
    private double longitude;
    private String name;
    private String description;
    private String price;
    private String quantity;
    private HomeViewModel homeViewModel;
    String[] productCategories;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productCategories = getResources().getStringArray(R.array.product_categories);
        firebaseStorageHelper = new FirebaseStorageHelper(requireContext());
        locationHelper = new LocationHelper(this, location -> {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        });
        locationHelper.requestLocationUpdates();
        firebaseDBHelper = FirebaseDBHelper.getInstance();
        homeViewModel = ((App) requireActivity().getApplication()).getHomeViewModel();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdateProductsBinding.inflate(inflater, container, false);
        db = new DatabaseHandler(requireContext(), listener);
        return binding.getRoot();
    }

    DatabaseHandler.DatabaseHelperListener listener = new DatabaseHandler.DatabaseHelperListener() {
        @Override
        public void onError(String message) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }

        public void insertRecord() {
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();
        initSetupCategory();

        binding.btnAddProduct.setOnClickListener(v -> {
            MyFragmentUtil.hideKeyboard(requireActivity());
            name = binding.edtProductName.getText().toString().trim();
            description = binding.edtProductDescription.getText().toString().trim();
            price = binding.edtPrice.getText().toString().trim();
            quantity = binding.edtQuantity.getText().toString().trim();
            boolean validateTextField = EditTextValidator.areTextFieldsValid(name, description, price, quantity);
            if (!validateTextField) {
                Snackbar.make(binding.clMain, getString(R.string.missing_field), Snackbar.LENGTH_LONG).show();
            } else {
                show(binding.includeLoader.getRoot());
                if (uploadImageUri!=null){
                    firebaseStorageHelper.uploadPicture(uploadImageUri,name,false);
                }else {
                    updateData();
                }
            }
        });
        binding.btnBack.setOnClickListener(v -> MyFragmentUtil.goBack(requireActivity()));

        binding.imgProductItem.setOnClickListener(v -> imageSelectionDialog.start());
        firebaseStorageHelper.setListener(new FirebaseStorageHelper.FirebaseStorageHelperInterface() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot, String publicUrl) {
                Log.e("Tag", "onSuccess()--> " + publicUrl);
                selectedImagePublicUrl=publicUrl;
                updateData();
            }

            @Override
            public void onError(String message) {
                hide(binding.includeLoader.getRoot());
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show());
            }
        });
        updateUI();

        binding.btnDelete.setOnClickListener(v -> ConfirmationDialog.showConfirmationDialog(requireContext(), getString(R.string.confirmation), getString(R.string.are_you_sure_you_want_to_proceed), () -> {
            LocalProductsModel selectedData = homeViewModel.getSelectedData();
            db.deleteRecord(selectedData.getProductId());
            firebaseDBHelper.deleteData(requireContext(),selectedData.getNodeKey());
            hide(binding.includeLoader.getRoot());
            Toast.makeText(requireContext(),  getString(R.string.product_update_successfully), Toast.LENGTH_LONG).show();
            binding.btnBack.performClick();
        }));
    }

    private void updateData() {
        LocalProductsModel localProductsModel = getLocalProductsModel(name, description, price, quantity);
        db.updateRecord(localProductsModel);
        firebaseDBHelper.updateData(localProductsModel.getNodeKey(),localProductsModel);
        hide(binding.includeLoader.getRoot());
        Toast.makeText(requireContext(), getString(R.string.product_update_successfully), Toast.LENGTH_LONG).show();
        binding.btnBack.performClick();
    }


    public void updateUI(){
        LocalProductsModel localProductsModel= homeViewModel.getSelectedData();
        binding.edtProductName.setText(localProductsModel.getProductName());
        binding.edtProductDescription.setText(localProductsModel.getProductDescription());
        binding.edtPrice.setText(localProductsModel.getProductPrice());
        binding.edtQuantity.setText(localProductsModel.getProductQuantity());
        Glide.with(requireActivity())
                .load(localProductsModel.getProductImage()).centerCrop()
                .error(R.drawable.ic_profile_holder)
                .into(binding.imgProductItem);
        int position = adapter.getPosition(localProductsModel.getProductCategory());
        if (position!=-1) {
            binding.spinnerProduct.setSelection(position);
        }
    }

    @NonNull
    private LocalProductsModel getLocalProductsModel(String name, String description, String price, String quantity) {
        LocalProductsModel localProductsModel = homeViewModel.getSelectedData();
        localProductsModel.setProductName(name);
        localProductsModel.setProductPrice(price);
        localProductsModel.setProductQuantity(quantity);
        localProductsModel.setProductLocation(latitude + "," + longitude);
        if (selectedImagePublicUrl!=null && selectedImagePublicUrl.length() != 0) {
            localProductsModel.setProductImage(selectedImagePublicUrl);
        }
        localProductsModel.setProductCategory(selectedCategory);
        localProductsModel.setProductDescription(description);
        return localProductsModel;
    }

    private void initDialog() {
        imageSelectionDialog = new ImageSelectionDialog(this, binding.imgProductItem, new ImageSelectionDialog.DialogClickListener() {
            @Override
            public void onCameraClick(Uri selectedImageUri) {
                uploadImageUri = selectedImageUri;
            }

            @Override
            public void onGalleryClick(Uri uri) {
                uploadImageUri = uri;
            }
        });
    }

    private void initSetupCategory() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, productCategories);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.spinnerProduct.setAdapter(adapter);
        // Set a listener to handle item selections
        selectedCategory = productCategories[0];
        binding.spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected category
                if (position >= 0 && position < productCategories.length) {
                    selectedCategory = productCategories[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSelectionDialog.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageSelectionDialog.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
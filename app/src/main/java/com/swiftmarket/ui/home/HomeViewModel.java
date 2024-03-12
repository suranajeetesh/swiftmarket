package com.swiftmarket.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.swiftmarket.data.LocalProductsModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    public MutableLiveData<String> totalAmount= new MutableLiveData<>("0");
    private final ArrayList<LocalProductsModel> cartProductList = new ArrayList<>();
    private ArrayList<LocalProductsModel> fullList = new ArrayList<>();
    private int selectedIndex=0;
    private LocalProductsModel selectedData;
    private final DecimalFormat decimalFormat= new DecimalFormat("##.##");

    public HomeViewModel(@NonNull Application application) {
        super(application);
        // Initialize your ViewModel
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public void addToCart(LocalProductsModel product){
        boolean isSameProduct=false;
        int sameProductIndex=-1;
        for (int i = 0; i < cartProductList.size(); i++) {
         if (Objects.equals(cartProductList.get(i).getProductImage(), product.getProductImage())){
             isSameProduct = true;
             sameProductIndex=i;
             break;
         }
        }
        if (!isSameProduct) {
            cartProductList.add(product);
        }else{
            LocalProductsModel localProductsModel = cartProductList.get(sameProductIndex);
            int productQuantity = Integer.parseInt(localProductsModel.getProductQuantity()) + Integer.parseInt(product.getProductQuantity());
            localProductsModel.setProductQuantity(String.valueOf(productQuantity));
        }
        calculateTheTotalAmount();
    }

    public MutableLiveData<String> getTotalAmount() {
        return totalAmount;
    }

    public ArrayList<LocalProductsModel> getCartProductList() {
        return cartProductList;
    }

    public void calculateTheTotalAmount(){
        double amount = 0.0;
        for (LocalProductsModel product : cartProductList) {
            int quantity = Integer.parseInt(product.getProductQuantity());
            double price = Double.parseDouble(product.getProductPrice());
            amount += quantity * price;
        }
        totalAmount.postValue(decimalFormat.format(amount));
    }

    public void setFullList(ArrayList<LocalProductsModel> fullList) {
        this.fullList = fullList;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public void clear() {
        cartProductList.clear();
        totalAmount.setValue("");
    }

    public LocalProductsModel getSelectedData() {
        return selectedData;
    }

    public void setSelectedData(LocalProductsModel selectedData) {
        this.selectedData = selectedData;
    }

}
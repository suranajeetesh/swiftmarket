package com.swiftmarket.data;

import androidx.annotation.NonNull;

import com.swiftmarket.utils.helper.DatabaseHandler;

/**
 * Created by Sam Reddy.
 */
public class LocalProductsModel implements Cloneable {
    private int productId; // Add this field for a unique identifier
    private String productName = DatabaseHandler.PRODUCT_NAME;
    private String productPrice = DatabaseHandler.PRODUCT_PRICE;
    private String productQuantity = DatabaseHandler.PRODUCT_QUANTITY;
    private String productLocation = DatabaseHandler.PRODUCT_LOCATION;
    private String productImage = DatabaseHandler.PRODUCT_IMAGE;
    private String productCategory = DatabaseHandler.PRODUCT_CATEGORY;
    private String productDescription = DatabaseHandler.PRODUCT_DESCRIPTION;
    private String nodeKey;

    // Constructor without productId
    public LocalProductsModel() {
    }

    // Constructor to initialize all values
    public LocalProductsModel(String productName, String productPrice,
                              String productQuantity, String productLocation,
                              String productImage, String productCategory,
                              String productDescription) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productLocation = productLocation;
        this.productImage = productImage;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @NonNull
    @Override
    public LocalProductsModel clone() {
        try {
            return (LocalProductsModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // This should never happen
        }
    }
}

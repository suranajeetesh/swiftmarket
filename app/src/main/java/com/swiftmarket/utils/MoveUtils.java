package com.swiftmarket.utils;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.swiftmarket.R;
import com.swiftmarket.ui.about.AboutUsFragment;
import com.swiftmarket.ui.activity.UserAccessActivity;
import com.swiftmarket.ui.products.AddProductsFragment;
import com.swiftmarket.ui.cart.CartFragment;
import com.swiftmarket.ui.home.HomeFragment;
import com.swiftmarket.ui.payment.PaymentFragment;
import com.swiftmarket.ui.product_details.ProductDetailsFragment;
import com.swiftmarket.ui.products.UpdateProductsFragment;
import com.swiftmarket.ui.success.SuccessFragment;
import com.swiftmarket.ui.userAccess.ResetPasswordFragment;
import com.swiftmarket.ui.userAccess.SignupFragment;
import com.swiftmarket.utils.helper.MyFragmentUtil;

/**
 * Utility class for navigating between fragments and activities.
 * Created by Sam.
 */
public class MoveUtils {
    // Move to Sign Up fragment
    public static void moveSignUP(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new SignupFragment(), true, true);
    }

    // Move to Forgot Password fragment
    public static void moveForgotPassword(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new ResetPasswordFragment(), true, true);
    }

    // Move to Home fragment
    public static void moveHomeFragment(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new HomeFragment(), true, true);
    }

    // Move to Product Details fragment
    public static void moveProductDetail(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new ProductDetailsFragment(), true, true);
    }

    // Move to Add to Cart fragment
    public static void moveAddToCart(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new CartFragment(), true, true);
    }

    // Move to Add Product fragment
    public static void moveAddProduct(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new AddProductsFragment(), true, true);
    }

    // Move to Update Product fragment
    public static void moveUpdateProduct(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new UpdateProductsFragment(), true, true);
    }

    // Move to Payment fragment
    public static void movePayment(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new PaymentFragment(), true, true);
    }

    // Move to Success fragment
    public static void moveSuccess(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new SuccessFragment(), true, true);
    }

    // Move to About Us fragment
    public static void moveAboutUs(FragmentActivity activity){
        MyFragmentUtil.addReplaceFragment(activity, R.id.fl_container, new AboutUsFragment(), true, true);
    }

    // Move to UserAccessActivity
    public static void moveUserAccessActivity(AppCompatActivity activity){
        activity.startActivity(new Intent(activity, UserAccessActivity.class));
        activity.finish();
    }
}

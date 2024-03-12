package com.swiftmarket.ui.activity;

import static com.swiftmarket.utils.Constant.KEY_EMAIL;
import static com.swiftmarket.utils.Constant.KEY_USER_PROFILE_URL;
import static com.swiftmarket.utils.MoveUtils.moveAboutUs;
import static com.swiftmarket.utils.MoveUtils.moveAddProduct;
import static com.swiftmarket.utils.MoveUtils.moveAddToCart;
import static com.swiftmarket.utils.MoveUtils.moveHomeFragment;
import static com.swiftmarket.utils.MoveUtils.moveUserAccessActivity;
import static com.swiftmarket.utils.helper.Preferences.clearPreference;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.swiftmarket.R;
import com.swiftmarket.core.BaseActivity;
import com.swiftmarket.databinding.ActivityMainBinding;
import com.swiftmarket.ui.cart.CartFragment;
import com.swiftmarket.ui.home.HomeFragment;
import com.swiftmarket.utils.helper.MyFragmentUtil;
import com.swiftmarket.utils.helper.Preferences;


public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Preferences.getPreferences(this);
        setContentView(binding.getRoot());
        MyFragmentUtil.addReplaceFragment(this, R.id.fl_container, new HomeFragment(),
                false, false);

        setupProfileImage();

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                manageDrawerView(false);
                int id = menuItem.getItemId();
                if (id == R.id.nav_home) {
                    moveHomeFragment(MainActivity.this);
                } else if (id == R.id.nav_cart) {
                    moveAddToCart(MainActivity.this);
                } else if (id == R.id.nav_addproduct) {
                    moveAddProduct(MainActivity.this);
                } else if (id == R.id.nav_about) {
                    moveAboutUs(MainActivity.this);
                } else if (id == R.id.nav_logout) {
                    clearPreference(MainActivity.this);
                    moveUserAccessActivity(MainActivity.this);
                }
                return true;
            }
        });
    }

    private void setupProfileImage() {
        String profileImage = Preferences.readString(MainActivity.this, KEY_USER_PROFILE_URL, "");
        String email = Preferences.readString(MainActivity.this, KEY_EMAIL, "");
        // Access the header view
        View headerView = binding.navView.getHeaderView(0);
        TextView txtEmail = headerView.findViewById(R.id.txt_username);
        txtEmail.setText(email);
        // Access the image view in the header view
        ImageView headerImageView = headerView.findViewById(R.id.profile_image);
        Glide.with(MainActivity.this)
                .load(profileImage).centerCrop()
                .error(R.drawable.ic_profile_holder)
                .into(headerImageView);
    }


    public void manageDrawerView(Boolean isOpen) {
        if (isOpen) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
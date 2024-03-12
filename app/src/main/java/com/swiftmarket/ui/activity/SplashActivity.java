package com.swiftmarket.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.swiftmarket.databinding.ActivitySplashBinding;
import com.swiftmarket.core.BaseActivity;
import com.swiftmarket.utils.Constant;
import com.swiftmarket.utils.helper.Preferences;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private SharedPreferences sharedPreferences;

    int time = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = Preferences.getPreferences(this);
        String email = Preferences.readString(this, Constant.KEY_EMAIL, "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!email.isEmpty()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, UserAccessActivity.class));
                    finish();
                }
            }
        }, time);
    }
}
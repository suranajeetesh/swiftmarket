package com.swiftmarket.ui.activity;

import android.os.Bundle;

import com.swiftmarket.R;
import com.swiftmarket.databinding.ActivityUserAccessBinding;
import com.swiftmarket.core.BaseActivity;
import com.swiftmarket.ui.userAccess.LoginFragment;
import com.swiftmarket.utils.helper.MyFragmentUtil;

public class UserAccessActivity extends BaseActivity {
    private ActivityUserAccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserAccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MyFragmentUtil.addReplaceFragment(this, R.id.fl_container, new LoginFragment(),
                false, false);
    }
}
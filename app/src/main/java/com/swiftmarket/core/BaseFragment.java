package com.swiftmarket.core;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.swiftmarket.utils.helper.MyFragmentUtil;

/**
 * Created by Sam.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyFragmentUtil.hideKeyboard(requireActivity());
    }
}

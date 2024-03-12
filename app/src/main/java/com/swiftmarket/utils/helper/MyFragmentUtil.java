package com.swiftmarket.utils.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
public class MyFragmentUtil {
    /**
     * Add replace fragment
     *
     * @param container
     * @param fragment
     * @param addFragment
     * @param addToBackStack
     */
    public static void addReplaceFragment(
            FragmentActivity activity,
            @IdRes int container,
            Fragment fragment,
            boolean addFragment,
            boolean addToBackStack
    ) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (addFragment) {
            transaction.add(container, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.replace(container, fragment, fragment.getClass().getSimpleName());
        }
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getTag());
        }
        hideKeyboard(activity);
        if (!activity.getSupportFragmentManager().isDestroyed()) {
            transaction.commit();
        }
    }


    // Hide the keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Go back from previous fragment
    public static void goBack(FragmentActivity activity){
        activity.getSupportFragmentManager().popBackStack();
    }
    // Go back on first fragment
    public static void goBackOnFirstFragment(FragmentActivity activity){
        activity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
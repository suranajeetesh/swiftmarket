package com.swiftmarket.utils;

import android.content.Context;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//Utility class for common view-related operations.
public class ViewUtils {
    /**
     * Make the specified view visible.
     *
     * @param view The view to be made visible.
     */
    public static void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    /**
     * Show or hide the specified view based on the provided boolean value.
     *
     * @param view   The view to be shown or hidden.
     * @param isShow Boolean value indicating whether to show or hide the view.
     */
    public static void show(View view, boolean isShow) {
        if (isShow) {
            show(view);
        } else {
            hide(view);
        }
    }

    /**
     * Make the specified view invisible (GONE).
     *
     * @param view The view to be made invisible.
     */
    public static void hide(View view) {
        view.setVisibility(View.GONE);
    }

    /**
     * Get the current timestamp as a formatted string.
     *
     * @return A formatted string representing the current timestamp.
     */
    public static String getCurrentTimestampAsString() {
        // Get the current date and time
        Date currentDate = new Date();
        // Define the format for the timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());

        // Format the date as a string
        return dateFormat.format(currentDate);
    }
}


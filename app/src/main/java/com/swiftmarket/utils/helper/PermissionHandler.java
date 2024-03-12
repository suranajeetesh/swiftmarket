package com.swiftmarket.utils.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionHandler {
    public static int requestCode = 123;
    // Check if a permission is granted
    public static boolean isPermissionsGranted(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false; // If any permission is not granted, return false
            }
        }
        return true; // All permissions are granted
    }

    // Request multiple permissions
    public static void requestPermissions(Fragment fragment, String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }
}

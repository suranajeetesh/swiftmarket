package com.swiftmarket.utils.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * Create by Sam
 * Helper class for managing location-related functionality.
 */
public class LocationHelper {

    private static final String TAG = "LocationHelper";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private Fragment fragment;
    private LocationListener locationListener;

    // GPS and network status flags
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    //Interface for receiving location updates.
    public interface LocationListener {
        void onLocationUpdated(Location location);
    }

    /**
     * Constructor for the LocationHelper class.
     *
     * @param fragment          The fragment requesting location updates.
     * @param locationListener  Listener for location updates.
     */
    public LocationHelper(Fragment fragment, LocationListener locationListener) {
        this.fragment = fragment;
        this.locationListener = locationListener;
    }

    //Request location updates, checking and requesting necessary permissions.
    public void requestLocationUpdates() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (PermissionHandler.isPermissionsGranted(fragment.requireActivity(), permissions)) {
            startLocationUpdates();
        } else {
            PermissionHandler.requestPermissions(fragment, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Handle the result of a permission request.
     *
     * @param requestCode   The request code.
     * @param permissions   The requested permissions.
     * @param grantResults  The results of the permission request.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Log.e(TAG, "Location permission denied");
            }
        }
    }

    //Start location updates based on available providers.
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationManager locationManager = (LocationManager) fragment.requireActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if (locationListener != null) {
                        locationListener.onLocationUpdated(location);
                    }
                }
            });
        } else if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (locationListener != null) {
                        locationListener.onLocationUpdated(location);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
    }
}

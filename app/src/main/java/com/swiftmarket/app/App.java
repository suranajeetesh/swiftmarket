package com.swiftmarket.app;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.swiftmarket.ui.home.HomeViewModel;

/**
 * Created by Sam.
 */
public class App extends Application {
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        homeViewModel = new ViewModelProvider.AndroidViewModelFactory(this)
                .create(HomeViewModel.class);
    }

    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }
}

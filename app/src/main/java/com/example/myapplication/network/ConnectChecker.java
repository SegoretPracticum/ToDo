package com.example.myapplication.network;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.myapplication.interfaces.ConnectCheck;

public class ConnectChecker implements ConnectCheck {
    private final ConnectivityManager connectivityManager;

    public ConnectChecker(Context context) {
        connectivityManager = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
    }

    @Override
    public Boolean isOffline() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo == null || !networkInfo.isConnected());
    }
}
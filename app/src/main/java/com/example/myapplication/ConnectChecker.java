package com.example.myapplication;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectChecker implements ConnectCheck {
    private final Context context;

    public ConnectChecker(Context context) {
        this.context = context;
    }

    @Override
    public Boolean isOffline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo == null || !networkInfo.isConnected());
    }
}
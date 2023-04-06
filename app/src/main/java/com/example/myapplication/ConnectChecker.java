package com.example.myapplication;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectChecker {
    private final Context context;

    public ConnectChecker(Context context) {
        this.context = context;
    }

    public boolean isOffline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo == null || !networkInfo.isConnected());
        }
        return true;
    }
}

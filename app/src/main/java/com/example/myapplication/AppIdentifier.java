package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class AppIdentifier implements AppIdentification{
    private final SharedPreferences sharedPreferences;
    private final static String APP_ID_FROM_SERVER = "App ServerID";
    private String appIdFromServer;

    public AppIdentifier(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_ID_FROM_SERVER,Context.MODE_PRIVATE);
    }

    @Override
    public void setAppIdFromServer(String appIdFromServer){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_ID_FROM_SERVER, appIdFromServer);
        editor.apply();
        this.appIdFromServer = appIdFromServer;
    }

    @Override
    public String getAppID() {
            return sharedPreferences.getString(APP_ID_FROM_SERVER,appIdFromServer);
    }
}
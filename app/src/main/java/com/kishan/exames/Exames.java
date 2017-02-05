package com.kishan.exames;

import android.app.Application;

import com.firebase.client.Firebase;


public class Exames extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

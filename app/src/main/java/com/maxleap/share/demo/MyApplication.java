package com.maxleap.share.demo;

import android.app.Application;
import android.content.Context;

import com.maxleap.social.MLHermes;

/**
 */
public class MyApplication extends Application {

    private String APP_ID = "564d6a6153e70e00012cf262";
    private String API_KEY = "UnBxUVJScDBFQ3FBZG4zaHlPenZ3UQ";

    @Override
    public void onCreate() {
        super.onCreate();

        MLHermes.initialize(this, APP_ID, API_KEY);
    }


}

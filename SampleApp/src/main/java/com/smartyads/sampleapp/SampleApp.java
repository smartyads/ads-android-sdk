package com.smartyads.sampleapp;

import android.app.Application;
import android.content.Context;

import com.smartyads.Ads;

public class SampleApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Ads.init(base);
    }

}

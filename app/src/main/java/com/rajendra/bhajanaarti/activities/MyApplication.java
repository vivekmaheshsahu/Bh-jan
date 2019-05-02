package com.rajendra.bhajanaarti.activities;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.rajendra.bhajanaarti.R;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }
}

package com.rajendra.bhajanaarti.activities

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

import com.google.android.gms.ads.MobileAds
import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.constants.Constant

class MyApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        Constant.APP_CONTEXT = this
        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id))
    }
}


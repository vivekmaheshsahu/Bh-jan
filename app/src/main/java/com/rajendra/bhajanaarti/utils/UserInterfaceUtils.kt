package com.rajendra.bhajanaarti.utils

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Typeface
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.util.*

object UserInterfaceUtils {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun loadAd(adView: AdView?){
        val adRequest = AdRequest.Builder()
                .addTestDevice("FD9F133038F995D8A876271BC9EBFCC0")
                .build()
        adView?.loadAd(adRequest)
    }

    fun assets(context: Context): Typeface {
        val am: AssetManager = context.assets
        val typeface: Typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "icomoon.ttf"))
        return typeface
    }
}

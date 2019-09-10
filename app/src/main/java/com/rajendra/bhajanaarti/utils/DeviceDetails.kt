package com.rajendra.bhajanaarti.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Log

import com.rajendra.bhajanaarti.base.BaseContextWrapper
import com.rajendra.bhajanaarti.constants.Constant

import java.util.Locale

object DeviceDetails {

    fun setLocale(langCode: String) {
        try {
            val locale = Locale(langCode)
            Locale.setDefault(locale)

            if (Constant.APP_CONTEXT != null) {
                val resources = Constant.APP_CONTEXT?.resources
                val dm = resources?.displayMetrics
                val conf = resources?.configuration



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    conf?.setLocale(locale)
                } else {
                    conf?.locale = locale
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (conf != null) {
                        Constant.APP_CONTEXT?.createConfigurationContext(conf)
                    }
                } else {
                    resources?.updateConfiguration(conf, dm)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("test", "exception " + e.message)
        }

    }

    fun setLocaleForApiBelow24() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            setLocale(Constant.LANG_CODE)
        }
    }

    fun setLocaleForApi24AndAbove(context: Context): ContextWrapper {
        val locale = Locale(Constant.LANG_CODE)
        Locale.setDefault(locale)
        return BaseContextWrapper.wrap(context, locale)
    }
}

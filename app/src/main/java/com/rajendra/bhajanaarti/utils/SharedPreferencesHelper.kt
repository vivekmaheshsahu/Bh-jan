package com.calibehr.mitra.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.rajendra.bhajanaarti.constants.Constant

object SharedPreferencesHelper {
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    fun addPref(context: Context?, key: String, value: String) {
        if (context != null) {
            pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE)
            editor = pref?.edit()
            editor?.putString(key, value)
            editor?.apply()
        }
    }

    fun addPref(context: Context?, key: String, value: Boolean?) {
        if (context != null) {
            pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE)
            editor = pref?.edit()
            editor?.putBoolean(key, value!!)
            editor?.apply()
        }
    }

    fun addPref(context: Context?, key: String, value: Int?) {
        if (context != null) {
            pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE)
            editor = pref?.edit()
            editor?.putInt(key, value!!)
            editor?.apply()
        }
    }

    fun getPref(context: Context?, key: String): String? {
        if (context != null) {
            pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE)
            //return pref!!.getString(key, "")
            return pref?.getString(key, "")
        }
        return ""
    }

    fun getPrefBoolean(context: Context?, key: String): Boolean? {
        if (context != null) {
            pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE)
            return pref?.getBoolean(key, false)
        }
        return false
    }

    fun getPrefInt(context: Context?, key: String): Int? {
        if (context != null) {
            pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE)
            return pref?.getInt(key, 0)
        }
        return 0
    }

    fun removePref(context: Context?, key: String) {
        if (context != null) {
            pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE)
            editor = pref?.edit()
            editor?.remove(key)
            editor?.apply()
        }
    }

    fun clearAll(context: Context?) {
        if (context != null) {
            pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE)
            editor = pref?.edit()
            editor?.clear()
            editor?.apply()
        }
    }
}
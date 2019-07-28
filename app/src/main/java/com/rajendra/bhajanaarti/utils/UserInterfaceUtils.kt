package com.rajendra.bhajanaarti.utils

import android.content.Context
import android.widget.Toast

object UserInterfaceUtils {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

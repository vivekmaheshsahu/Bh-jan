package com.rajendra.bhajanaarti.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.rajendra.bhajanaarti.constants.Constant
import com.rajendra.bhajanaarti.utils.DeviceDetails

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // keep the Application window size FULL and display ON
        super.onCreate(savedInstanceState)
        Constant.APP_CONTEXT = this
        DeviceDetails.setLocaleForApiBelow24()
        setContentView(provideLayoutId())
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(DeviceDetails.setLocaleForApi24AndAbove(newBase))
    }

    @LayoutRes
    protected abstract fun provideLayoutId(): Int
}
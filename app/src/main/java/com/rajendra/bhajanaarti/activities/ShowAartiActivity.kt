package com.rajendra.bhajanaarti.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils
import kotlinx.android.synthetic.main.activity_show_aarti.*


class ShowAartiActivity : AppCompatActivity() {
    private var mAdView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_aarti)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "सुखकर्ता दुखहर्ता"

        mAdView = findViewById(R.id.adView)
        UserInterfaceUtils.loadAd(mAdView)

        tvPreArrow.typeface = UserInterfaceUtils.assets(this)
        tvNxtArrow.typeface = UserInterfaceUtils.assets(this)

        try {
            val res = resources
            val in_s = res.openRawResource(R.raw.sukhkarta_mar)

            val b = ByteArray(in_s.available())
            in_s.read(b)
            txt.text = String(b)
        } catch (e: Exception) {
            // e.printStackTrace();
            txt.setText("Error: can't show help.")
        }
    }



    fun back(view: View) {

    }

    fun next(view: View) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }
}

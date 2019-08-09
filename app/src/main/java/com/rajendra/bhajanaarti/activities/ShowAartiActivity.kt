package com.rajendra.bhajanaarti.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils
import kotlinx.android.synthetic.main.activity_show_aarti.*


class ShowAartiActivity : AppCompatActivity(), View.OnClickListener {
    private var mAdView: AdView? = null
    private var aartiName: String? = null
    private var aarti_text: Int? = null
    private var scrollShowAarti: ScrollView? = null
    private var tvPreArrow: TextView? = null;
    private var tvNxtArrow: TextView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_aarti)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent.extras
        aartiName = intent?.getString("aarti_name")
        aarti_text = intent?.getInt("aarti_text")

        initializer()
    }


    private fun initializer() {
        mAdView = findViewById(R.id.adView)
        UserInterfaceUtils.loadAd(mAdView)

        supportActionBar?.title = aartiName

        tvPreArrow = findViewById(R.id.tvPreArrow)
        tvNxtArrow = findViewById(R.id.tvNxtArrow)

        tvPreArrow?.setOnClickListener(this)
        tvNxtArrow?.setOnClickListener(this)

        tvPreArrow?.typeface = UserInterfaceUtils.assets(this)
        tvNxtArrow?.typeface = UserInterfaceUtils.assets(this)

        txt.text = getAartiFromRaw(aarti_text)

        scrollShowAarti = findViewById(R.id.scrollShowAarti);
    }


    fun getAartiFromRaw(raw: Int?): String{
        try {
            val res = resources
            val in_s = res.openRawResource(raw!!)

            val b = ByteArray(in_s.available())
            in_s.read(b)
            return String(b)
        } catch (e: Exception) {
             e.printStackTrace();
        }
        return ""
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvPreArrow -> {

            }
            R.id.tvNxtArrow -> {

            }
        }
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

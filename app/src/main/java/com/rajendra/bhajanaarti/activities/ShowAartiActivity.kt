package com.rajendra.bhajanaarti.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.Pojo.Album
import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.base.BaseActivity
import com.rajendra.bhajanaarti.utils.OnSwipeTouchListener
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils


class ShowAartiActivity : BaseActivity(){
    override fun provideLayoutId(): Int {
        return R.layout.activity_show_aarti
    }

    private val TAG = "ShowAartiActivity"
    private var mAdView: AdView? = null
    private var aartiName: String? = null
    private var aarti_text: Int? = null
    private var scrollShowAarti: ScrollView? = null
    private var listOfAarti: MutableList<Album>? = null
    private var aartiIndex: Int? = null
    private var tvPreArrow:TextView?=null
    private var tvNxtArrow:TextView?=null
    private var txt:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvPreArrow = findViewById(R.id.tvPreArrow)
        tvNxtArrow = findViewById(R.id.tvNxtArrow)
        txt = findViewById(R.id.txt)

        val intent = intent.extras
        aartiName = intent?.getString("aarti_name")
        aarti_text = intent?.getInt("aarti_text")
        listOfAarti = intent?.getParcelableArrayList<Album>("listOfAartis")
        aartiIndex = intent?.getInt("aartiIndex")

        if (aartiIndex == 0)
            tvPreArrow?.setTextColor(Color.GRAY)
        else if (aartiIndex == (listOfAarti?.size?.dec())){
            tvNxtArrow?.setTextColor(Color.GRAY)
        }
        initializer()
    }

    override fun onResume() {
        super.onResume()
        UserInterfaceUtils.loadAd(mAdView)
    }

    private fun initializer() {
        mAdView = findViewById(R.id.adView)
        UserInterfaceUtils.loadAd(mAdView)
        supportActionBar?.title = aartiName
        tvPreArrow?.typeface = UserInterfaceUtils.assets(this)
        tvNxtArrow?.typeface = UserInterfaceUtils.assets(this)
        txt?.text = getAartiFromRaw(aarti_text)
        scrollShowAarti = findViewById(R.id.scrollShowAarti)

        scrollShowAarti?.setOnTouchListener(object : OnSwipeTouchListener(applicationContext){
            override fun onSwipeLeft() {
                //super.onSwipeLeft()
                try {
                    showNextAarti()
                }
                catch (e: Exception){
                    e.printStackTrace()
                    Log.d(TAG, "Exception_onSwipeLeft " + e.message)
                }
            }
            override fun onSwipeRight() {
                //super.onSwipeRight()
                try {
                    showPreAarti()
                }
                catch (e: Exception){
                    e.printStackTrace()
                    Log.d(TAG, "Exception_onSwipeRight " + e.message)
                }
            }
        })
    }


    fun getAartiFromRaw(raw: Int?): String{
        try {
            val res = resources
            val in_s = res.openRawResource(raw!!)

            val b = ByteArray(in_s.available())
            in_s.read(b)
            return String(b)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "Exception_getAartiFromRaw " + e.message)
        }
        return ""
    }

    fun back(view: View){
        showPreAarti()
    }

    fun next(view: View){
        showNextAarti()
    }

    fun showNextAarti(){
        UserInterfaceUtils.loadAd(mAdView)
        if (aartiIndex!! < listOfAarti!!.size.dec()){
            aartiIndex = aartiIndex?.inc()
            tvNxtArrow?.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.holo_green_dark))
            tvPreArrow?.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.holo_green_dark))
            supportActionBar?.title = listOfAarti?.get(aartiIndex!!)?.name
            txt?.text = getAartiFromRaw(listOfAarti?.get(aartiIndex!!)?.raw)
        }
        if (aartiIndex == (listOfAarti?.size?.dec())){
            tvNxtArrow?.setTextColor(Color.GRAY)
        }
        Log.d("test","nxtClick_index " + aartiIndex)
        scrollShowAarti?.fullScroll(View.FOCUS_UP)
        scrollShowAarti?.smoothScrollTo(0,0)
    }

    fun showPreAarti(){
        UserInterfaceUtils.loadAd(mAdView)
        if (aartiIndex!! >= 1){
            aartiIndex = aartiIndex?.dec()
            tvPreArrow?.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.holo_green_dark))
            tvNxtArrow?.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.holo_green_dark))
            supportActionBar?.title = listOfAarti?.get(aartiIndex!!)?.name
            txt?.text = getAartiFromRaw(listOfAarti?.get(aartiIndex!!)?.raw)
        }

        if (aartiIndex == 0){
            tvPreArrow?.setTextColor(Color.GRAY)
        }
        Log.d("test","prevClick_index " + aartiIndex)
        scrollShowAarti?.fullScroll(View.FOCUS_UP)
        scrollShowAarti?.smoothScrollTo(0,0)
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

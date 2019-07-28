package com.rajendra.bhajanaarti.activities

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.rajendra.bhajanaarti.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class FeedbackActivity : AppCompatActivity(), View.OnClickListener {


    private var mAdView: AdView? = null
    lateinit var btn_feedback: Button
    lateinit var etMail_feedback_message: EditText
    lateinit var emailMessege: String
    private var tvVersion: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        mAdView = findViewById<View>(R.id.adView) as AdView
        val adRequest = AdRequest.Builder()
                .build()
        mAdView!!.adListener = object : AdListener() {
            override fun onAdLoaded() {

            }

            override fun onAdClosed() {
                Log.d("FeedbackActivity", "Ad is closed!")
            }

            override fun onAdFailedToLoad(i: Int) {
                Log.d("FeedbackActivity", "onAdFailedToLoad")
            }

            override fun onAdLeftApplication() {
                Log.d("FeedbackActivity", "onAdLeftApplication")
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }
        }

        mAdView!!.loadAd(adRequest)

        tvVersion = findViewById(R.id.tvVersion)
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            if (pInfo != null)
                tvVersion!!.text = "Ver: " + pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        etMail_feedback_message = findViewById<View>(R.id.etMail_feedback_message) as EditText

        btn_feedback = findViewById<View>(R.id.btn_feedback) as Button
        btn_feedback.setOnClickListener(this)
    }

    override fun onPause() {
        if (mAdView != null) {
            mAdView!!.pause()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (mAdView != null) {
            mAdView!!.resume()
        }
    }

    override fun onDestroy() {
        if (mAdView != null) {
            mAdView!!.destroy()
        }
        super.onDestroy()
    }

    override fun onClick(v: View) {
        if (v === btn_feedback) {
            emailMessege = etMail_feedback_message.text.toString()

            if (emailMessege.equals("", ignoreCase = true)) {
                Toast.makeText(this, "Please Enter Feedback", Toast.LENGTH_SHORT).show()
            } else {
                sendFeedback()
            }
        }
    }

    private fun sendFeedback() {
        val feedbckIntent = Intent(android.content.Intent.ACTION_SEND)
        feedbckIntent.type = "text/html"
        feedbckIntent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_feedback_email)))
        feedbckIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject))
        feedbckIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailMessege)
        startActivity(Intent.createChooser(feedbckIntent, getString(R.string.title_send_feedback)))
    }

    companion object {
        private val TAG = "MainActivity"
    }


}

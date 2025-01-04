package com.rajendra.bhajanaarti.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.VersionedPackage
import android.icu.util.VersionInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.AdView

import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils
import java.util.*

class FeedbackFragment : Fragment(), View.OnClickListener {

    private var mAdView: AdView? = null
    lateinit var btn_feedback: Button
    lateinit var etMail_feedback_message: EditText
    lateinit var emailMessege: String
    private var tvVersion: TextView? = null

    companion object {
        private val TAG = "FeedbackFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feeback, container, false)
        initializer(view)
        return view
    }

    fun initializer(v: View){
        mAdView = v.findViewById<View>(R.id.adView) as AdView
        UserInterfaceUtils.loadAd(mAdView)

        tvVersion = v.findViewById(R.id.tvVersion)
        try {
            val pInfo = activity?.packageManager?.getPackageInfo(activity!!.packageName, 0);
            if (pInfo != null)
                tvVersion!!.text = String.format(Locale.US, "%s: %s","Ver", pInfo.versionName)

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        etMail_feedback_message = v.findViewById<View>(R.id.etMail_feedback_message) as EditText

        btn_feedback = v.findViewById<View>(R.id.btn_feedback) as Button
        btn_feedback.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v === btn_feedback) {
            emailMessege = etMail_feedback_message.text.toString()

            if (emailMessege.equals("", ignoreCase = true)) {
                Toast.makeText(activity, "Please Enter Feedback", Toast.LENGTH_SHORT).show()
            } else {
                sendFeedback()
            }
        }
    }

    override fun onPause() {
        if (mAdView != null) {
            mAdView!!.pause()
        }
        UserInterfaceUtils.hideSoftKeyboard(activity)
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
            mAdView?.destroy()
        }
        UserInterfaceUtils.hideSoftKeyboard(activity)
        super.onDestroy()
    }

    private fun sendFeedback() {
        val feedbckIntent = Intent(Intent.ACTION_SEND)
        feedbckIntent.type = "text/html"
        feedbckIntent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_feedback_email)))
        feedbckIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject))
        feedbckIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailMessege)
        startActivity(Intent.createChooser(feedbckIntent, getString(R.string.title_send_feedback)))
    }
}

package com.rajendra.bhajanaarti.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.net.Uri
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import androidx.fragment.app.Fragment

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.google.firebase.analytics.FirebaseAnalytics
import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.constants.Constant
import com.rajendra.bhajanaarti.firebase.NotificationHelper
import com.rajendra.bhajanaarti.fragments.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.calibehr.mitra.utils.SharedPreferencesHelper
import com.google.android.material.internal.NavigationMenuView
import com.rajendra.bhajanaarti.base.BaseActivity


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal var TAG = "HomeActivity"
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mInterstitialAd: InterstitialAd? = null

    companion object {
        lateinit var analytics: GoogleAnalytics
        lateinit var tracker: Tracker
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Song List")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Song name click")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        mFirebaseAnalytics!!.setUserProperty("favorite_food", "Paneer")
        analytics = GoogleAnalytics.getInstance(this)
        analytics.setLocalDispatchPeriod(1800)
        tracker = analytics.newTracker("UA-88365539-1")
        tracker.enableExceptionReporting(true)
        tracker.enableAdvertisingIdCollection(true)
        tracker.enableAutoActivityTracking(true)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd?.adUnitId = getString(R.string.interstitial_ad)
        loadAd()
        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad")
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened")
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication")
            }

            override fun onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed")
                // load next ad
                loadAd()
            }
        }

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val navMenuView = navigationView.getChildAt(0) as NavigationMenuView
        navMenuView.addItemDecoration(DividerItemDecoration(this@HomeActivity, DividerItemDecoration.VERTICAL))
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null
        navigationView.setCheckedItem(R.id.navMusic)
        displaySelectedScreen(R.id.navMusic)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        val hindi = menu?.getItem(1)
        val english = menu?.getItem(2)
        if (Constant.LANG_CODE.equals("hi")){
            hindi?.setChecked(true)
        }else if (Constant.LANG_CODE.equals("en")){
            english?.setChecked(true)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_hindi -> {
                if (item.isChecked())
                    item.setChecked(false)
                else
                    item.setChecked(true)

                Constant.LANG_CODE = "hi"
                saveLangCodeAndRestartActivity()
            }
            R.id.action_english ->{
                if (item.isChecked())
                    item.setChecked(false)
                else
                    item.setChecked(true)

                Constant.LANG_CODE = "en"
                saveLangCodeAndRestartActivity()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

        if (MusicPlayerActivity.mp != null) {
            MusicPlayerActivity.mp?.stop()
            MusicPlayerActivity.mp?.release()
            MusicPlayerActivity.mp = null
        }
        Constant.NOW_PLAYING_SONG_NAME = ""

        return true
    }

    fun saveLangCodeAndRestartActivity(){
        SharedPreferencesHelper.addPref(this@HomeActivity, Constant.SP_LANG_CODE, Constant.LANG_CODE)
        startActivity(Intent(this@HomeActivity, HomeActivity::class.java))
        finish()
    }

    fun loadAd(){
        val adRequest = AdRequest.Builder()
                //.addTestDevice("FD9F133038F995D8A876271BC9EBFCC0")
                .build()
        mInterstitialAd?.loadAd(adRequest)
    }

    override fun onResume() {
        super.onResume()
        NotificationHelper.clearNotifications(this)
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else {
            if (mInterstitialAd!!.isLoaded)
                mInterstitialAd?.show()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displaySelectedScreen(item.itemId)
        return true
    }

    private fun displaySelectedScreen(itemId: Int) {
        var fragment: Fragment? = null

        when (itemId) {
            R.id.navMusic ->{
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.listen_bhajan)
                fragment = HomeFragment()
            }

            R.id.navHinAartiEng -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.devi_aarti)
                if (Constant.LANG_CODE.equals("en")){
                    Constant.LANGUAGE = "hindi"
                }
                else
                    Constant.LANGUAGE = "हिंदी"

                fragment = AartiFragment()
            }

            R.id.navOurApps -> {
                val uri = Uri.parse(Constant.OUR_APPS_LINK)
                val ourAppsIntent = Intent(Intent.ACTION_VIEW, uri)
                // After pressing back button from google play will continue to app
                ourAppsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                startActivity(ourAppsIntent)
            }

            R.id.navShare -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, Constant.PLAY_STORE_LINK)
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

            R.id.navFeedback -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.feedback_or_suggestions)
                fragment = FeedbackFragment()
            }

            R.id.navUpdate -> {
                val uri = Uri.parse(Constant.PLAY_STORE_LINK)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                // After pressing back button from google play will continue to app
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                startActivity(goToMarket)
            }

            R.id.navExit -> {
                if (MusicPlayerActivity.mp != null) {
                    MusicPlayerActivity.mp?.stop()
                    MusicPlayerActivity.mp?.release()
                    MusicPlayerActivity.mp = null
                }
                Constant.NOW_PLAYING_SONG_NAME = ""
                val exitIntent = Intent(Intent.ACTION_MAIN)
                exitIntent.addCategory(Intent.CATEGORY_HOME)
                exitIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(exitIntent)
                finish()
            }

            R.id.navRateUs ->{
                val uri = Uri.parse(Constant.PLAY_STORE_LINK)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                // After pressing back button from google play will continue to app
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                startActivity(goToMarket)
            }

            R.id.navDisclaimer -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.disclaimer)
                fragment = DisclaimerFragment()
            }
        }
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_home, fragment)
            ft.commit()
        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy_called")
    }
}

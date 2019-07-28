package com.rajendra.bhajanaarti.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.net.Uri
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.google.firebase.analytics.FirebaseAnalytics
import com.rajendra.bhajanaarti.Adapters.SongInfoAdapter
import com.rajendra.bhajanaarti.Pojo.SongInfo
import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.constants.Constant
import com.rajendra.bhajanaarti.firebase.NotificationHelper

import java.util.ArrayList
import java.util.Locale

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {

    internal var TAG = "HomeActivity"
    lateinit internal var adapter: SongInfoAdapter
    private var mInterstitialAd: InterstitialAd? = null
    lateinit internal var listSongs: ListView
    lateinit internal var songInfo: MutableList<SongInfo>
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var progressBar: ProgressBar? = null
    internal var handler = Handler()
    internal var songIndex: Int = 0
    private var ivPlayHome: ImageView? = null
    private var ivPauseHome: ImageView? = null

    internal var r: Runnable = Runnable {
        progressBar!!.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        val intent = Intent(this@HomeActivity, MusicPlayerActivity::class.java)
        intent.putExtra("songindex", songIndex)
        startActivity(intent)
    }

    companion object {
        lateinit var analytics: GoogleAnalytics
        lateinit var tracker: Tracker
        val songName = arrayOf("Ambe Tu Hai Jagdambe Kali", "Bheja Hai Bulava Tune Sherawaliye",
                "Bhor Bhai Din Char Gaya Meri Ambe", "Bigdi Meri Bana De", "Durga Hai Meri Maa",
                "Hey Naam Re Sabse Bada Tera Naam", "Kabse Khadi Hoon", "Maa Sun Le Pukar",
                "Maiya Ka Chola Hai Rangla", "Maiya Main Nihaal Ho Gaya", "Meri Akhiyon Ke Samne Hi Rehna",
                "Meri Jholi Chhoti Pad Gayee Re", "Na Main Mangu Sona Devi Bhajan", "Pyara Saja Hai Tera Dwar",
                "Suno Suno Ek Kahani")
        val imageid = R.drawable.bhajan
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        analytics = GoogleAnalytics.getInstance(this)
        analytics.setLocalDispatchPeriod(1800)
        tracker = analytics.newTracker("UA-88365539-1")
        tracker.enableExceptionReporting(true)
        tracker.enableAdvertisingIdCollection(true)
        tracker.enableAutoActivityTracking(true)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = getString(R.string.interstitial_ad)
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
        mInterstitialAd!!.adListener = object : AdListener() {
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
                mInterstitialAd!!.loadAd(AdRequest.Builder().build())
            }
        }

        val mAdView = findViewById<View>(R.id.adView) as AdView
        val adRequest = AdRequest.Builder()
                .build()
        //.addTestDevice("FD9F133038F995D8A876271BC9EBFCC0")
        mAdView.loadAd(adRequest)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null

        ivPlayHome = findViewById(R.id.ivPlayHome)
        ivPauseHome = findViewById(R.id.ivPauseHome)

        songInfo = ArrayList()
        for (i in songName.indices) {
            val item = SongInfo(imageid, songName[i])
            songInfo.add(item)
        }

        listSongs = findViewById<View>(R.id.listSongs) as ListView
        adapter = SongInfoAdapter(this, R.layout.content_home, songInfo)
        listSongs.adapter = adapter
        listSongs.onItemClickListener = this
        progressBar = findViewById(R.id.progressBar)
    }


    override fun onResume() {
        super.onResume()

        NotificationHelper.clearNotifications(this)
        if (Constant.NOW_PLAYING_SONG_NAME != null && Constant.NOW_PLAYING_SONG_NAME.length > 1) {
            val playingLayout = findViewById<RelativeLayout>(R.id.playingLayout)
            playingLayout.visibility = View.VISIBLE
            if (MusicPlayerActivity.mp!!.isPlaying) {
                showPlayButton(false)
            } else {
                showPlayButton(true)
            }
            playingLayout.setOnClickListener(this)

            val playingSongName = findViewById<TextView>(R.id.playingSongName)
            playingSongName.setText(String.format(Locale.US, "%s %s",
                    "Now playing: ", Constant.NOW_PLAYING_SONG_NAME))
            playingSongName.isSelected = true
        }
    }

    fun showPlayButton(show: Boolean) {
        if (show) {
            ivPlayHome!!.visibility = View.VISIBLE
            ivPauseHome!!.visibility = View.INVISIBLE
        } else {
            ivPlayHome!!.visibility = View.INVISIBLE
            ivPauseHome!!.visibility = View.VISIBLE
        }

    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else {
            if (mInterstitialAd!!.isLoaded)
                mInterstitialAd!!.show()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deviSongDrawer -> {
                val intent = Intent(this@HomeActivity, HomeActivity::class.java)
                startActivity(intent)
            }

            R.id.aartiDrawer -> {
                val intent1 = Intent(this@HomeActivity, Written_Aarti::class.java)
                startActivity(intent1)
            }

            R.id.nav_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, Constant.PLAY_STORE_LINK)
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

            R.id.feedbackDrawer -> startActivity(Intent(this@HomeActivity, FeedbackActivity::class.java))

            R.id.updateDrawer -> {
                val uri = Uri.parse(Constant.PLAY_STORE_LINK)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                // After pressing back button from google play will continue to app
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                startActivity(goToMarket)
            }

            R.id.exitDrawer -> {
                if (MusicPlayerActivity.mp != null) {
                    MusicPlayerActivity.mp!!.stop()
                    MusicPlayerActivity.mp!!.release()
                    MusicPlayerActivity.mp = null
                }
                Constant.NOW_PLAYING_SONG_NAME = ""
                val exitIntent = Intent(Intent.ACTION_MAIN)
                exitIntent.addCategory(Intent.CATEGORY_HOME)
                exitIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(exitIntent)
                finish()
            }
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        progressBar!!.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        Log.d(TAG, "SongInfo " + songInfo[position].imageid)
        Log.d(TAG, "SongInfo_name " + songInfo[position].songname!!)

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Song List")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Song name click")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        mFirebaseAnalytics!!.setUserProperty("favorite_food", "Paneer")

        songIndex = adapter.getItemId(position).toInt()

        Log.d(TAG, "position_index $songIndex")
        if (MusicPlayerActivity.mp != null) {
            MusicPlayerActivity.mp!!.stop()
            MusicPlayerActivity.mp = null
        }

        handler.postDelayed(r, 900)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.playingLayout -> {
                val intent = Intent(this@HomeActivity, MusicPlayerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy_called")
    }
}

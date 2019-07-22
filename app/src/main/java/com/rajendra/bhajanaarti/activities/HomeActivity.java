package com.rajendra.bhajanaarti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.rajendra.bhajanaarti.Adapters.SongInfoAdapter;
import com.rajendra.bhajanaarti.Pojo.SongInfo;
import com.rajendra.bhajanaarti.R;
import com.rajendra.bhajanaarti.constants.Constant;
import com.rajendra.bhajanaarti.firebase.NotificationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {

    String TAG = "HomeActivity";
    SongInfoAdapter adapter;
    public static GoogleAnalytics analytics;
    private InterstitialAd mInterstitialAd;
    public static Tracker tracker;
    public static final String[] songName = new String[] {"Ambe Tu Hai Jagdambe Kali",
            "Bheja Hai Bulava Tune Sherawaliye", "Bhor Bhai Din Char Gaya Meri Ambe",
            "Bigdi Meri Bana De", "Durga Hai Meri Maa",
            "Hey Naam Re Sabse Bada Tera Naam", "Kabse Khadi Hoon",
            "Maa Sun Le Pukar" ,"Maiya Ka Chola Hai Rangla",
            "Maiya Main Nihaal Ho Gaya", "Meri Akhiyon Ke Samne Hi Rehna",
            "Meri Jholi Chhoti Pad Gayee Re", "Na Main Mangu Sona Devi Bhajan",
            "Pyara Saja Hai Tera Dwar", "Suno Suno Ek Kahani"};

    public static final Integer imageid = R.drawable.bhajan;
    ListView listSongs;
    List<SongInfo> songInfo;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ProgressBar progressBar;
    Handler handler = new Handler();
    int songIndex;
    private ImageView ivPlayHome, ivPauseHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);
        tracker = analytics.newTracker("UA-88365539-1");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
                // load next ad
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        //.addTestDevice("FD9F133038F995D8A876271BC9EBFCC0")
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ivPlayHome = findViewById(R.id.ivPlayHome);
        ivPauseHome = findViewById(R.id.ivPauseHome);

        songInfo = new ArrayList<SongInfo>();
        for (int i = 0; i < songName.length; i++)
        {
            SongInfo item = new SongInfo(imageid, songName[i]);
            songInfo.add(item);
        }

        listSongs = (ListView) findViewById(R.id.listSongs);
        adapter = new SongInfoAdapter(this,R.layout.content_home, songInfo);
        listSongs.setAdapter(adapter);
        listSongs.setOnItemClickListener(this);
        progressBar = findViewById(R.id.progressBar);
    }


    @Override
    protected void onResume() {
        super.onResume();

        NotificationHelper.clearNotifications(this);
        if (Constant.NOW_PLAYING_SONG_NAME != null && Constant.NOW_PLAYING_SONG_NAME.length() > 1){
            RelativeLayout playingLayout = findViewById(R.id.playingLayout);
            playingLayout.setVisibility(View.VISIBLE);
            if (MusicPlayerActivity.mp.isPlaying()) {
                showPlayButton(false);
            }
            else {
                showPlayButton(true);
            }
            playingLayout.setOnClickListener(this);

            TextView playingSongName = findViewById(R.id.playingSongName);
            playingSongName.setText(String.format(Locale.US, "%s %s",
                    "Now playing: ", Constant.NOW_PLAYING_SONG_NAME));
            playingSongName.setSelected(true);
        }
    }

    public void showPlayButton(boolean show){
        if (show){
            ivPlayHome.setVisibility(View.VISIBLE);
            ivPauseHome.setVisibility(View.INVISIBLE);
        }
        else {
            ivPlayHome.setVisibility(View.INVISIBLE);
            ivPauseHome.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
        {
            if (mInterstitialAd.isLoaded())
                mInterstitialAd.show();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.deviSongDrawer:
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.aartiDrawer:
                Intent intent1 = new Intent(HomeActivity.this,Written_Aarti.class);
                startActivity(intent1);
                break;

            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Constant.PLAY_STORE_LINK);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.feedbackDrawer:
                startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));
                break;

            case R.id.updateDrawer:
                Uri uri = Uri.parse(Constant.PLAY_STORE_LINK);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // After pressing back button from google play will continue to app
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(goToMarket);
                break;

            case R.id.exitDrawer:
                if (MusicPlayerActivity.mp != null){
                    MusicPlayerActivity.mp.stop();
                    MusicPlayerActivity.mp.release();
                    MusicPlayerActivity.mp = null;
                }
                Constant.NOW_PLAYING_SONG_NAME = "";
                Intent exitIntent = new Intent(Intent.ACTION_MAIN);
                exitIntent.addCategory(Intent.CATEGORY_HOME);
                exitIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exitIntent);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Log.d(TAG,"SongInfo " + songInfo.get(position).getImageid());
        Log.d(TAG,"SongInfo_name " + songInfo.get(position).getSongname());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Song List");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Song name click");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        mFirebaseAnalytics.setUserProperty("favorite_food", "Paneer");

        songIndex = (int) adapter.getItemId(position);

        Log.d(TAG, "position_index " + songIndex);
        if (MusicPlayerActivity.mp != null){
            MusicPlayerActivity.mp.stop();
            MusicPlayerActivity.mp = null;
        }

        handler.postDelayed(r, 900);
    }

    Runnable r = new Runnable() {
        public void run() {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Intent intent = new Intent(HomeActivity.this, MusicPlayerActivity.class);
            intent.putExtra("songindex", songIndex);
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playingLayout:
                Intent intent = new Intent(HomeActivity.this, MusicPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy_called");
    }
}

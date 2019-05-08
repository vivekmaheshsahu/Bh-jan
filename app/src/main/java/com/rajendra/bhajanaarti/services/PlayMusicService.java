package com.rajendra.bhajanaarti.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.rajendra.bhajanaarti.activities.MusicPlayerActivity;
import com.rajendra.bhajanaarti.utils.Utilities;

public class PlayMusicService extends Service {

    private final String TAG = "PlayMusicService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (MusicPlayerActivity.mp != null){
            if (MusicPlayerActivity.mp.isPlaying()) {
                MusicPlayerActivity.mp.pause();
                Utilities.pause = true;

                Log.d(TAG, "Music has pause");
            } else {
                Utilities.pause = false;
                MusicPlayerActivity.mp.start();
                Log.d(TAG, "Music has start");
            }
        }
        return Service.START_STICKY;
    }

    public void onStart(Intent intent, int startId) { }

    public IBinder onUnBind(Intent arg0) { return null; }

    public void onStop() { }

    public void onPause() { }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy_called");
        Utilities.playing = false;
        if (MusicPlayerActivity.mp != null){
            MusicPlayerActivity.mp.stop();
            MusicPlayerActivity.mp.release();

            Log.d(TAG, "Music has stopped and release");
        }
    }

    @Override
    public void onLowMemory() {

    }
}

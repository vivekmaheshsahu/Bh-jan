package com.rajendra.bhajanaarti.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.rajendra.bhajanaarti.activities.MusicPlayerActivity

class PhoneCallStateReceiver : BroadcastReceiver() {
    private val TAG = "PhoneCallStateReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            Log.d(TAG, "EXTRA_STATE_RINGING")
            pauseSong()
        } else if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
            // Call received
            Log.d(TAG, "EXTRA_STATE_OFFHOOK")
        } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
            // Call Dropped or rejected
            Log.d(TAG, "EXTRA_STATE_IDLE")
            playSong()
        }
    }

    private fun pauseSong() {
        if (MusicPlayerActivity.mp != null) {
            if (MusicPlayerActivity.mp!!.isPlaying) {
                MusicPlayerActivity.mp!!.pause()
                Log.d(TAG, "Call came so music has pause")
            }
        }
    }

    private fun playSong() {
        if (MusicPlayerActivity.mp != null) {
            Log.d(TAG, "call kept so music started")
            MusicPlayerActivity.mp!!.start()
        }
    }
}

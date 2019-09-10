package com.rajendra.bhajanaarti.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.rajendra.bhajanaarti.activities.MusicPlayerActivity
import android.telephony.PhoneStateListener

class PhoneCallStateReceiver : BroadcastReceiver() {
    private val TAG = "PhoneCallStateReceiver"

    var telManager: TelephonyManager? = null
    var context: Context? = null

    override fun onReceive(context: Context, intent: Intent) {
        this.context = context
        telManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        telManager?.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private val phoneListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            try {
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        Log.d(TAG, "CALL_STATE_RINGING")
                        pauseSong()
                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {

                    }
                    TelephonyManager.CALL_STATE_IDLE -> {
                        // Call Dropped or rejected
                        Log.d(TAG, "CALL_STATE_IDLE")
                        playSong()
                    }
                    else -> {

                    }
                }
            } catch (ex: Exception) {
                Log.d(TAG, "Exception_PhoneStateListener " + ex.message)
            }

        }
    }

    private fun pauseSong() {
        if (MusicPlayerActivity.mp != null) {
            if (MusicPlayerActivity.mp?.isPlaying!!) {
                MusicPlayerActivity.mp?.pause()
                Log.d(TAG, "Call came so music has pause")
            }
        }
    }

    private fun playSong() {
        if (MusicPlayerActivity.mp != null) {
            Log.d(TAG, "call kept so music started")
            MusicPlayerActivity.mp?.start()
        }
    }
}

package com.rajendra.bhajanaarti.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.rajendra.bhajanaarti.services.PlayMusicService;
import com.rajendra.bhajanaarti.utils.Utilities;

public class PhoneCallStateReceiver extends BroadcastReceiver {
    private final String TAG = "PhoneCallStateReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number = "";
        Bundle bundle = intent.getExtras();


        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            // Phone is ringing
            number = bundle.getString("incoming_number");
            Log.d("testing", number);
            stopServices(context);



        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            // Call received

        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // Call Dropped or rejected


        }
    }

    private void stopServices(Context context) {
        // TODO Auto-generated method stub
        if (Utilities.playing) {
            Log.e("start services", "start services");
            Intent i = new Intent(context, PlayMusicService.class);
            context.startService(i);
        } else {
            Log.e("start not services", "start not services");
        }
    }
}

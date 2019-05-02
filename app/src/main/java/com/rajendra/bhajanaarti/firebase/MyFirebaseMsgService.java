package com.rajendra.bhajanaarti.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rajendra.bhajanaarti.utils.NotificationHelper;

public class MyFirebaseMsgService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        Log.d("token", "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            Log.d("onMessageReceived", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String updateMsg = remoteMessage.getNotification().getBody();
            if (updateMsg!= null && updateMsg.length() > 0){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    NotificationHelper.createNotificationChannel(this);
                }
                NotificationHelper.showNotification(this, updateMsg);
            }
        }
    }
}

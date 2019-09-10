package com.rajendra.bhajanaarti.firebase

import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMsgService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        try {
            if (token.isNotEmpty())
                Log.d("token", "Refreshed token: " + token)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null) {
            Log.d("onMessageReceived", "Message Notification Body: " + remoteMessage.notification!!.body!!)
            if (remoteMessage.notification != null) {
                val notiTitle = remoteMessage.notification!!.title
                val notiText = remoteMessage.notification!!.body
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationHelper.createNotificationChannel(this)
                }
                NotificationHelper.showNotification(this, notiTitle!!, notiText!!)
            }
        }
    }
}

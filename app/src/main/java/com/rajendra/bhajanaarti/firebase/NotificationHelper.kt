package com.rajendra.bhajanaarti.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.constants.Constant

object NotificationHelper {
    private val CHANNEL_ID = "channel_id_01"
    private val NAME = "Bhajan_noti"
    private val DESCRIPTION = "Bhjan aarti description"

    fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, NAME, importance)
            mChannel.description = DESCRIPTION
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(mChannel)
        }
    }


    fun showNotification(context: Context, notiTitle: String, notiText: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            val p = getPendindIntent(context, notiTitle)
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            builder.setSmallIcon(R.drawable.bhajan)
            builder.setLargeIcon(BitmapFactory.decodeResource(context.resources,
                    R.drawable.bhajan))
            builder.setContentTitle(Constant.APP_NAME)
            builder.setDefaults(Notification.DEFAULT_ALL)
            builder.priority = Notification.PRIORITY_HIGH
            builder.priority = NotificationCompat.PRIORITY_HIGH
            builder.setContentTitle(notiTitle)
            builder.setContentText(notiText)
            builder.setSound(alarmSound)
            builder.setAutoCancel(true)
            builder.setVibrate(longArrayOf(0))
            if (p != null)
                builder.setContentIntent(p)
            notificationManager.notify(Constant.NOTIFY_ID, builder.build())

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getPendindIntent(context: Context, notiTitle: String): PendingIntent? {
        if (notiTitle.contains(Constant.UPDATE)) {
            val uri = Uri.parse(Constant.PLAY_STORE_LINK)
            val updateAppIntent = Intent()
            updateAppIntent.action = Intent.ACTION_VIEW
            // After pressing back button from google play will continue to app
            updateAppIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            updateAppIntent.data = uri
            return PendingIntent.getActivity(context, Constant.PENDING_INTENT_REQUEST_CODE, updateAppIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return null
    }

    fun clearNotifications(context: Context?) {
        if (context != null) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }
    }
}

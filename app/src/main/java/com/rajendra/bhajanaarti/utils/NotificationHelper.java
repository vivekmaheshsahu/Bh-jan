package com.rajendra.bhajanaarti.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.rajendra.bhajanaarti.R;
import com.rajendra.bhajanaarti.constants.Constant;

public class NotificationHelper {
    private static final String CHANNEL_ID = "channel_id_01";
    private static final String NAME = "Bhajan_noti";
    private static final String DESCRIPTION = "Bhjan aarti description";

    public static void createNotificationChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, NAME, importance);
            mChannel.setDescription(DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(mChannel);
        }
    }


    public static void showNotification(Context context, String notiTitle, String notiText){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            PendingIntent p = getPendindIntent(context, notiTitle);
                Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                builder.setSmallIcon(R.drawable.bhajan);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.bhajan));
                builder.setContentTitle(Constant.APP_NAME);
                builder.setDefaults(Notification.DEFAULT_ALL);
                builder.setPriority(Notification.PRIORITY_HIGH);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setContentTitle(notiTitle);
                builder.setContentText(notiText);
                builder.setSound(alarmSound);
                builder.setAutoCancel(true);
                builder.setVibrate(new long[]{0});
                if (p != null)
                    builder.setContentIntent(p);
                notificationManager.notify(Constant.NOTIFY_ID, builder.build());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PendingIntent getPendindIntent(Context context, String notiTitle){
        if (notiTitle.contains(Constant.UPDATE)){
            Uri uri = Uri.parse(Constant.PLAY_STORE_LINK);
            Intent updateAppIntent = new Intent();
            updateAppIntent.setAction(Intent.ACTION_VIEW);
            // After pressing back button from google play will continue to app
            updateAppIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            updateAppIntent.setData(uri);
            return PendingIntent.getActivity(context, Constant.PENDING_INTENT_REQUEST_CODE, updateAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return null;
    }

    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}

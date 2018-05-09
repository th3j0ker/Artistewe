package com.typhon.studios.artistewe_prototype_3.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.typhon.studios.artistewe_prototype_3.Likes.LikesActivity;
import com.typhon.studios.artistewe_prototype_3.R;

public class NotificationsHelper {

    String CHANNEL_ID;
    Context context;

    public NotificationsHelper(String CHANNEL_ID, Context context) {
        this.CHANNEL_ID = CHANNEL_ID;
        this.context = context;
    }

    public NotificationsHelper() {
        //required
    }

    public void showNotification(String title, String content) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_artistewe_001_test_latest_002) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                //.setSound(alarmSound) // set alarm sound for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(context, LikesActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

}

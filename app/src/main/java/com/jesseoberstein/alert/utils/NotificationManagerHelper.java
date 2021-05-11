package com.jesseoberstein.alert.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.jesseoberstein.alert.R;

import javax.inject.Inject;

import static com.jesseoberstein.alert.utils.Constants.CHANNEL_ID;

public class NotificationManagerHelper {
    private final Context context;
    private final NotificationManager notificationManager;

    @Inject
    public NotificationManagerHelper(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationManager = notificationManager;
    }


    public void pushNotification(int notificationId, String headline, String title, String messageText, int color) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_train)
                .setSubText(headline)
                .setContentTitle(title)
                .setContentText(messageText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageText))
                .setColor(color)
                .setLocalOnly(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLights(color, 300, 1000)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify(notificationId, notification);
    }
}

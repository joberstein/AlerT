package com.jesseoberstein.alert;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.tasks.InitializeDatabaseTask;

import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.NotificationManager.IMPORTANCE_NONE;
import static com.jesseoberstein.alert.utils.Constants.CHANNEL_ID;
import static com.jesseoberstein.alert.utils.Constants.CHANNEL_NAME;

@HiltAndroidApp
public class MainApplication extends Application {

    @Inject
    AppDatabase appDatabase;

    @Inject
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        new InitializeDatabaseTask().execute(appDatabase);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);
            int channelImportance = Optional.ofNullable(channel)
                    .map(NotificationChannel::getImportance)
                    .orElse(IMPORTANCE_NONE);

            if (channelImportance == IMPORTANCE_NONE) {
                NotificationChannel newChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(newChannel);
            }
        }

//        TODO: Is this still necessary?
//        Intent intent = new Intent(this, MessagingService.class);
//        this.startService(intent);
    }
}
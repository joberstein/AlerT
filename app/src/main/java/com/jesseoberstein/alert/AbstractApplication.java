package com.jesseoberstein.alert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.services.MessagingService;
import com.jesseoberstein.alert.tasks.InitializeDatabaseTask;

import java.util.Optional;

import javax.inject.Inject;

import dagger.android.DaggerApplication;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.NotificationManager.IMPORTANCE_NONE;
import static com.jesseoberstein.alert.utils.Constants.CHANNEL_ID;
import static com.jesseoberstein.alert.utils.Constants.CHANNEL_NAME;

/**
 * On app startup, asynchronously initialize the db, which should populate it with MBTA data if not
 * already present.
 */
public abstract class AbstractApplication extends DaggerApplication {

    @Inject
    AppDatabase appDatabase;

    @Inject
    NotificationChannel notificationChannel;

    @Inject
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        new InitializeDatabaseTask().execute(appDatabase);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager nm = this.getSystemService(NotificationManager.class);
            NotificationChannel channel = nm.getNotificationChannel(CHANNEL_ID);
            int channelImportance = Optional.ofNullable(channel)
                    .map(NotificationChannel::getImportance)
                    .orElse(IMPORTANCE_NONE);

            if (channelImportance == IMPORTANCE_NONE) {
                NotificationChannel newChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH);
                nm.createNotificationChannel(newChannel);
            }
        }

        Intent intent = new Intent(this, MessagingService.class);
        this.startService(intent);
    }
}
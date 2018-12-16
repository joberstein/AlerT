package com.jesseoberstein.alert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.tasks.InitializeDatabaseTask;

import javax.inject.Inject;

import dagger.android.DaggerApplication;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
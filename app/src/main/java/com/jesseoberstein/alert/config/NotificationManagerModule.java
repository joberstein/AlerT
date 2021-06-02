package com.jesseoberstein.alert.config;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.jesseoberstein.alert.utils.NotificationManagerHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class NotificationManagerModule {

    @Singleton
    @Provides
    NotificationManager notificationManager(Application application) {
        return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

//    @Singleton
//    @Provides
//    NotificationChannel notificationChannel(Application application) {
//        CharSequence name = application.getString(R.string.channel_name);
//        String description = application.getString(R.string.channel_description);
//        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, IMPORTANCE_DEFAULT);
//        channel.setDescription(description);
//        return channel;
//    }

    @Singleton
    @Provides
    NotificationManagerHelper notificationManagerHelper(
            Application application,
            NotificationManager notificationManager
    ) {
        return new NotificationManagerHelper(application.getApplicationContext(), notificationManager);
    }
}

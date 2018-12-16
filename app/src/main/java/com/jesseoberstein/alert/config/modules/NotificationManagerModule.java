package com.jesseoberstein.alert.config.modules;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.utils.NotificationManagerHelper;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

import static android.app.NotificationManager.*;
import static com.jesseoberstein.alert.utils.Constants.CHANNEL_ID;

@Module
public class NotificationManagerModule {

    @Reusable
    @Provides
    NotificationManager notificationManager(Application application) {
        return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Reusable
    @Provides
    NotificationChannel notificationChannel(Application application) {
        CharSequence name = application.getString(R.string.channel_name);
        String description = application.getString(R.string.channel_description);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, IMPORTANCE_DEFAULT);
        channel.setDescription(description);
        return channel;
    }

    @Reusable
    @Provides
    NotificationManagerHelper notificationManagerHelper(Application application,
                                                        NotificationManager notificationManager) {
        return new NotificationManagerHelper(application.getApplicationContext(), notificationManager);
    }
}

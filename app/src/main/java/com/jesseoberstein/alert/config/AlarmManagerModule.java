package com.jesseoberstein.alert.config;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

import com.jesseoberstein.alert.utils.AlarmManagerHelper;
import com.jesseoberstein.alert.utils.IntentBuilder;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class AlarmManagerModule {

    @Singleton
    @Provides
    AlarmManager alarmManager(Application application) {
        return (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
    }

    @Singleton
    @Provides
    IntentBuilder intentBuilder(Application application) {
        return new IntentBuilder(application.getApplicationContext());
    }

    @Singleton
    @Provides
    AlarmManagerHelper alarmManagerHelper(
            IntentBuilder intentBuilder,
            AlarmManager alarmManager,
            UserAlarmScheduler userAlarmScheduler
    ) {
        return new AlarmManagerHelper(intentBuilder, alarmManager, userAlarmScheduler);
    }
}

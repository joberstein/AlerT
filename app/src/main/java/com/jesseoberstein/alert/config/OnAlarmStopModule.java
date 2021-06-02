package com.jesseoberstein.alert.config;

import android.app.Application;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.utils.OnAlarmStopHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module(includes = UserAlarmModule.class)
@InstallIn(SingletonComponent.class)
public final class OnAlarmStopModule {

    @Singleton
    @Provides
    static OnAlarmStopHelper onAlarmStopHelper(
            Application application,
            UserAlarmScheduler alarmScheduler,
            AppDatabase db
    ) {
        return new OnAlarmStopHelper(application.getApplicationContext(), alarmScheduler, db);
    }
}

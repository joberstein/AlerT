package com.jesseoberstein.alert.config;

import com.jesseoberstein.alert.utils.DateTimeHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import java.util.Calendar;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class UserAlarmModule {

    @Singleton
    @Provides
    UserAlarmScheduler userAlarmScheduler(DateTimeHelper dateTimeHelper, Calendar calendar) {
        return new UserAlarmScheduler(dateTimeHelper, calendar);
    }
}

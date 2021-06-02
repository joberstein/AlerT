package com.jesseoberstein.alert.config;

import com.jesseoberstein.alert.utils.DateTimeHelper;

import java.util.Calendar;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class DateTimeModule {

    @Singleton
    @Provides
    Calendar calendar() {
        return Calendar.getInstance();
    }

    @Singleton
    @Provides
    DateTimeHelper dateTimeUtils(Calendar calendar) {
        return new DateTimeHelper(calendar);
    }
}

package com.jesseoberstein.alert.config.modules;

import com.jesseoberstein.alert.utils.DateTimeHelper;

import java.util.Calendar;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public class DateTimeModule {

    @Reusable
    @Provides
    Calendar calendar() {
        return Calendar.getInstance();
    }

    @Reusable
    @Provides
    DateTimeHelper dateTimeUtils() {
        return new DateTimeHelper(calendar());
    }
}

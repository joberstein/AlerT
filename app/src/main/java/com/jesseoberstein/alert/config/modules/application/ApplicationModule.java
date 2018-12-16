package com.jesseoberstein.alert.config.modules.application;

import android.app.Application;

import com.jesseoberstein.alert.MainApplication;
import com.jesseoberstein.alert.config.modules.AlarmManagerModule;
import com.jesseoberstein.alert.config.modules.DatabaseModule;
import com.jesseoberstein.alert.config.modules.DateTimeModule;
import com.jesseoberstein.alert.config.modules.NetworkModule;
import com.jesseoberstein.alert.config.modules.NotificationManagerModule;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
    AlarmManagerModule.class,
    DatabaseModule.class,
    DateTimeModule.class,
    NetworkModule.class,
    NotificationManagerModule.class
})
public abstract class ApplicationModule {

    @Binds
    abstract Application bindApplication(MainApplication application);
}

package com.jesseoberstein.alert.config.modules.application;

import android.app.Application;

import com.jesseoberstein.alert.MainApplication;
import com.jesseoberstein.alert.config.modules.AlarmManagerModule;
import com.jesseoberstein.alert.config.modules.DatabaseModule;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
    AlarmManagerModule.class,
    DatabaseModule.class
})
public abstract class ApplicationModule {

    @Binds
    abstract Application bindApplication(MainApplication application);
}

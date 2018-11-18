package com.jesseoberstein.alert.config;

import android.app.Application;

import com.jesseoberstein.alert.TestApplication;
import com.jesseoberstein.alert.config.modules.AlarmManagerModule;
import com.jesseoberstein.alert.config.modules.DatabaseModule;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
    AlarmManagerModule.class,
    TestDatabaseModule.class
})
abstract class TestApplicationModule {

    @Binds
    abstract Application bindApplication(TestApplication application);
}

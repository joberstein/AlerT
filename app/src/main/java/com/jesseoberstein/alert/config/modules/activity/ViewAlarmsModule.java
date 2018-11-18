package com.jesseoberstein.alert.config.modules.activity;

import com.jesseoberstein.alert.activities.alarms.ViewAlarms;
import com.jesseoberstein.alert.activities.base.BaseActivity;
import com.jesseoberstein.alert.config.scopes.ActivityScope;

import dagger.Binds;
import dagger.Module;

@Module(includes = ActivityModule.class)
abstract class ViewAlarmsModule {

    @ActivityScope
    @Binds
    abstract BaseActivity viewAlarms(ViewAlarms activity);
}

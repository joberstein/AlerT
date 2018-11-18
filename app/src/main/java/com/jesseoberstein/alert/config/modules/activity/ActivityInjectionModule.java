package com.jesseoberstein.alert.config.modules.activity;

import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.activities.alarms.ViewAlarms;
import com.jesseoberstein.alert.config.scopes.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityInjectionModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = ViewAlarmsModule.class)
    abstract ViewAlarms viewAlarms();

    @ActivityScope
    @ContributesAndroidInjector(modules = EditAlarmModule.class)
    abstract EditAlarm editAlarm();
}

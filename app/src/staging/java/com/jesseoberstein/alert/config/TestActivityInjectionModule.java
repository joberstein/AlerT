package com.jesseoberstein.alert.config;

import com.jesseoberstein.alert.activity.ViewAlarmsWithPermissions;
import com.jesseoberstein.alert.config.scopes.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class TestActivityInjectionModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = TestActivityWithPermissionsModule.class)
    abstract ViewAlarmsWithPermissions viewAlarmsWithPermissions();
}

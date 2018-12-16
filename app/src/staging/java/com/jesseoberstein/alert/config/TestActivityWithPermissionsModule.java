package com.jesseoberstein.alert.config;

import com.jesseoberstein.alert.activities.base.BaseActivity;
import com.jesseoberstein.alert.activity.ViewAlarmsWithPermissions;
import com.jesseoberstein.alert.config.modules.activity.ActivityModule;
import com.jesseoberstein.alert.config.scopes.ActivityScope;

import dagger.Binds;
import dagger.Module;

@Module(includes = ActivityModule.class)
abstract class TestActivityWithPermissionsModule {

    @ActivityScope
    @Binds
    abstract BaseActivity viewAlarmsWithPermissions(ViewAlarmsWithPermissions activity);
}

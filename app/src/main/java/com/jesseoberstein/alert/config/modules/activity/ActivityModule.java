package com.jesseoberstein.alert.config.modules.activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

import com.jesseoberstein.alert.activities.base.BaseActivity;
import com.jesseoberstein.alert.config.modules.DateTimeModule;
import com.jesseoberstein.alert.config.modules.MbtaDataReceiverModule;
import com.jesseoberstein.alert.config.modules.UserAlarmModule;
import com.jesseoberstein.alert.config.scopes.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = {
    DateTimeModule.class,
    MbtaDataReceiverModule.class,
    UserAlarmModule.class
})
abstract class ActivityModule {

    @ActivityScope
    @Binds
    abstract Context activityContext(BaseActivity activity);

    @ActivityScope
    @Provides
    static FragmentManager fragmentManager(BaseActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @ActivityScope
    @Provides
    static ActionBar actionBar(BaseActivity activity) {
        return activity.getSupportActionBar();
    }
}

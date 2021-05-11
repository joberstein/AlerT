package com.jesseoberstein.alert.config.modules.activity;

import android.content.Context;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;

import com.jesseoberstein.alert.activities.base.BaseActivity;
import com.jesseoberstein.alert.config.modules.MbtaDataReceiverModule;
import com.jesseoberstein.alert.config.modules.UserAlarmModule;
import com.jesseoberstein.alert.config.scopes.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = {
    MbtaDataReceiverModule.class,
    UserAlarmModule.class
})
public abstract class ActivityModule {

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

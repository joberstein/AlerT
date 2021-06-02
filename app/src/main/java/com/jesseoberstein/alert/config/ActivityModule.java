package com.jesseoberstein.alert.config;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.scopes.ActivityScoped;

@Module
@InstallIn(ActivityComponent.class)
public final class ActivityModule {

    @ActivityScoped
    @Provides
    FragmentManager fragmentManager(Activity activity) {
        return ((AppCompatActivity) activity).getSupportFragmentManager();
    }

    @ActivityScoped
    @Provides
    ActionBar actionBar(Activity activity) {
        return ((AppCompatActivity) activity).getSupportActionBar();
    }
}

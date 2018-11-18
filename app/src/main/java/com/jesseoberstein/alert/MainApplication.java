package com.jesseoberstein.alert;

import com.jesseoberstein.alert.config.components.DaggerApplicationComponent;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.tasks.InitializeDatabaseTask;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * On app startup, asynchronously initialize the db, which should populate it with MBTA data if not
 * already present.
 */
public class MainApplication extends DaggerApplication {
    @Inject AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        new InitializeDatabaseTask().execute(appDatabase);
    }

    @Override
    protected AndroidInjector<MainApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().create(this);
    }
}
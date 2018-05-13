package com.jesseoberstein.alert;

import android.app.Application;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.tasks.InitializeDatabaseTask;

/**
 * On app startup, asynchronously initialize the db, which should populate it with MBTA data if not
 * already present.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        new InitializeDatabaseTask().execute(appDatabase);
    }
}
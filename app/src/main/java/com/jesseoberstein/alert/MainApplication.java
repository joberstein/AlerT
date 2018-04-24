package com.jesseoberstein.alert;

import android.app.Application;

import com.jesseoberstein.alert.data.DatabaseHelper;
import com.jesseoberstein.alert.services.InitializeDatabaseTask;

/**
 * On app startup, asynchronously initialize the db (including populating it with MBTA data).
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.init(this, false);
        DatabaseHelper.get().getConnectionSource();
        new InitializeDatabaseTask().execute();
    }
}
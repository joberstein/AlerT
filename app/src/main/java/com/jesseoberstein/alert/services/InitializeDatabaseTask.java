package com.jesseoberstein.alert.services;

import android.os.AsyncTask;

import com.jesseoberstein.alert.data.DatabaseHelper;

/**
 * An async task responsible for initializing the {@link DatabaseHelper#MBTA_DATABASE_NAME} db.
 */
public class InitializeDatabaseTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void[] objects) {
        DatabaseHelper dbHelper = DatabaseHelper.get();
        dbHelper.getWritableDatabase();
        dbHelper.close();
        return null;
    }
}


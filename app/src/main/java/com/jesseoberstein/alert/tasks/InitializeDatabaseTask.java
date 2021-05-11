package com.jesseoberstein.alert.tasks;

import android.os.AsyncTask;

import androidx.room.RoomDatabase;

import java.util.Arrays;

/**
 * An async task responsible for initializing any number of databases by opening them for writing.
 */
public class InitializeDatabaseTask extends AsyncTask<RoomDatabase, Void, Void> {

    @Override
    protected Void doInBackground(RoomDatabase[] databases) {
        Arrays.stream(databases).forEach(db -> db.getOpenHelper().getWritableDatabase());
        return null;
    }
}


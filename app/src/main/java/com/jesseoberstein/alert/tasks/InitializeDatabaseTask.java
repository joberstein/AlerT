package com.jesseoberstein.alert.tasks;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.os.AsyncTask;

import com.jesseoberstein.alert.data.database.AppDatabase;

import java.io.IOException;

/**
 * An async task responsible for initializing the {@link AppDatabase#ALERT_DATABASE_NAME} db.
 */
public class InitializeDatabaseTask extends AsyncTask<Void, Void, Void> {
    private AppDatabase database;

    public InitializeDatabaseTask(AppDatabase database) {
        this.database = database;
    }

    @Override
    protected Void doInBackground(Void[] objects) {
        this.database.getOpenHelper().getWritableDatabase();
        return null;
    }
}


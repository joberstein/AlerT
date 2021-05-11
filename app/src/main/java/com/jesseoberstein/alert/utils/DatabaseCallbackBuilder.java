package com.jesseoberstein.alert.utils;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jesseoberstein.alert.data.database.MbtaDatabase;

import java.io.IOException;

import javax.inject.Inject;

public class DatabaseCallbackBuilder {
    private final FileHelper fileHelper;

    @Inject
    public DatabaseCallbackBuilder(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    /**
     * Read in a SQL file that will pre-populate the tables from the MBTA database.
     * @return The callback that Room should execute when the database is created.
     */
    public RoomDatabase.Callback buildCreateDbCallback() {
        return new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                try {
                    fileHelper.readFile(MbtaDatabase.MBTA_DATABASE_FILENAME).forEach(db::execSQL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}

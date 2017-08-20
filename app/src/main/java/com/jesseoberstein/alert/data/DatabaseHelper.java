package com.jesseoberstein.alert.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jesseoberstein.alert.R;

import java.sql.SQLException;

abstract class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "alert";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

//    @Override
    <T> void onCreate(SQLiteDatabase database, ConnectionSource connectionSource, Class<T> daoClass) {
        try {
            Log.i(daoClass.getName(), "onCreate");
            TableUtils.createTable(connectionSource, daoClass);
        } catch (SQLException e) {
            Log.e(daoClass.getName(), "Can't create table", e);
            throw new RuntimeException(e);
        }

        Log.i(daoClass.getName(), "Created a new table");
    }


    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    <T> void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion, Class<T> daoClass) {
        try {
            Log.i(daoClass.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, daoClass, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(daoClass.getName(), "Can't drop database", e);
            throw new RuntimeException(e);
        }
    }

    static <T> T getInstance(T instance, T initialValue) {
        if (instance == null) {
            return initialValue;
        }
        return instance;
    }
}

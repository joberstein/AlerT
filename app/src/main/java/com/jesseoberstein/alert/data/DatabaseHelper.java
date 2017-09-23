package com.jesseoberstein.alert.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserEndpoint;
import com.jesseoberstein.alert.models.UserRoute;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

abstract class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "alert";
    private static final int DATABASE_VERSION = 13;
    private static List<Class<?>> DAO_CLASSES = Arrays.asList(
            UserRoute.class, UserAlarm.class, UserEndpoint.class
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onOpen(SQLiteDatabase database){
        super.onOpen(database);
        if (!database.isReadOnly()){
            database.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        DAO_CLASSES.forEach(daoClass -> createTable(database, connectionSource, daoClass));
    }

    private <T> void createTable(SQLiteDatabase database, ConnectionSource connectionSource, Class<T> daoClass) {
        String className = daoClass.getName();
        Log.i(className, "onCreate");

        try {
            TableUtils.createTable(connectionSource, daoClass);
        } catch (SQLException e) {
            Log.e(className, "Can't create table", e);
            throw new RuntimeException(e);
        }

        Log.i(className, "Created a new table");
    }


    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        DAO_CLASSES.forEach(daoClass -> {
            String className = daoClass.getName();
            Log.i(className, "onUpgrade");

            try {
                TableUtils.dropTable(connectionSource, daoClass, true);
                // after we drop the old databases, we create the new ones
                createTable(db, connectionSource, daoClass);
            } catch (SQLException e) {
                Log.e(className, "Can't drop database", e);
                throw new RuntimeException(e);
            }
        });
    }

    static <T> T getInstance(T instance, T initialValue) {
        if (instance == null) {
            return initialValue;
        }
        return instance;
    }
}

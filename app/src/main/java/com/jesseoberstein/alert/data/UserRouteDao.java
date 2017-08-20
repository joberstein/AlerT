package com.jesseoberstein.alert.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.jesseoberstein.alert.models.UserRoute;

public class UserRouteDao extends DatabaseHelper {
    private RuntimeExceptionDao<UserRoute, String> dao = null;

    public UserRouteDao(Context context) {
        super(context);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        this.onCreate(database, connectionSource, UserRoute.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        this.onUpgrade(db, connectionSource, oldVersion, newVersion, UserRoute.class);
    }


    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our UserRoute class.
     * It will create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<UserRoute, String> getDao() {
        dao = getInstance(dao, getRuntimeExceptionDao(UserRoute.class));
        return dao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        dao = null;
    }
}

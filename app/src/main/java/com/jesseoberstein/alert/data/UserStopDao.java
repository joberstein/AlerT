package com.jesseoberstein.alert.data;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jesseoberstein.alert.models.UserStop;

public class UserStopDao extends DatabaseHelper {
    private RuntimeExceptionDao<UserStop, String> dao = null;

    public UserStopDao(Context context) {
        super(context);
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our UserStop class.
     * It will create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<UserStop, String> getDao() {
        dao = getInstance(dao, getRuntimeExceptionDao(UserStop.class));
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

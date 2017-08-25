package com.jesseoberstein.alert.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.jesseoberstein.alert.models.UserAlarm;

public class UserAlarmDao extends DatabaseHelper {
    private RuntimeExceptionDao<UserAlarm, Integer> dao = null;

    public UserAlarmDao(Context context) {
        super(context);
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our UserAlarm class.
     * It will create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<UserAlarm, Integer> getDao() {
        dao = getInstance(dao, getRuntimeExceptionDao(UserAlarm.class));
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

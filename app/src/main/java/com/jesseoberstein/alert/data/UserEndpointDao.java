package com.jesseoberstein.alert.data;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jesseoberstein.alert.models.UserEndpoint;

public class UserEndpointDao extends DatabaseHelper {
    private RuntimeExceptionDao<UserEndpoint, Integer> dao = null;

    public UserEndpointDao(Context context) {
        super(context);
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our UserEndpoint class.
     * It will create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<UserEndpoint, Integer> getDao() {
        dao = getInstance(dao, getRuntimeExceptionDao(UserEndpoint.class));
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

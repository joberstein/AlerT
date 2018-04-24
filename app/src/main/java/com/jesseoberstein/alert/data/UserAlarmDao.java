package com.jesseoberstein.alert.data;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jesseoberstein.alert.models.UserAlarm;

public class UserAlarmDao {
    private static UserAlarmDao instance;
    private static RuntimeExceptionDao<UserAlarm, Integer> userAlarmDao;

    private UserAlarmDao() {
        userAlarmDao = DatabaseHelper.getUserAlarmDao();
    }

    public static RuntimeExceptionDao<UserAlarm, Integer> get() {
        return userAlarmDao;
    }

    static void init() {
        if (instance == null) {
            instance = new UserAlarmDao();
        }
    }
}

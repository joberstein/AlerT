package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jesseoberstein.alert.models.UserAlarm;

import java.util.List;

@Dao
public interface UserAlarmDao extends BaseDao<UserAlarm> {

    @Query("SELECT * FROM user_alarms WHERE id == :id")
    UserAlarm get(long id);

    @Override
    @Query("SELECT * FROM user_alarms")
    List<UserAlarm> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(UserAlarm[] alarm);

    @Override
    @Delete
    void delete(UserAlarm[] alarm);

    @Override
    @Query("SELECT COUNT(*) FROM user_alarms")
    long count();
}

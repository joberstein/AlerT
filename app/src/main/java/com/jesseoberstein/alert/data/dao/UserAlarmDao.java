package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.List;

@Dao
public interface UserAlarmDao extends BaseDao<UserAlarm> {

    @Query("SELECT * FROM user_alarms WHERE id == :id")
    UserAlarm get(long id);

    @Transaction
    @Query("SELECT * FROM user_alarms WHERE id IN (:ids)")
    List<UserAlarmWithRelations> getWithRelations(Long[] ids);

    @Query("SELECT * FROM user_alarms")
    List<UserAlarm> getAll();

    @Transaction
    @Query("SELECT * FROM user_alarms")
    List<UserAlarmWithRelations> getAllWithRelations();

    @Override
    @Query("SELECT COUNT(*) FROM user_alarms")
    long count();
}

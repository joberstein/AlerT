package com.jesseoberstein.alert.data.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UserAlarmDao extends BaseDao<UserAlarm> {

    @Query("SELECT * FROM user_alarms WHERE id == :id")
    UserAlarm get(long id);

    @Query("SELECT * FROM user_alarms WHERE id == :id")
    Single<UserAlarm> getSingle(long id);

    @Transaction
    @Query("SELECT * FROM user_alarms WHERE id IN (:ids)")
    List<UserAlarmWithRelations> getWithRelations(Long[] ids);

    @Transaction
    @Query("SELECT * FROM user_alarms WHERE id == :id")
    Single<UserAlarmWithRelations> getWithRelationsSingle(long id);

    @Query("SELECT * FROM user_alarms")
    List<UserAlarm> getAll();

    @Transaction
    @Query("SELECT * FROM user_alarms")
    List<UserAlarmWithRelations> getAllWithRelations();

    @Override
    @Query("SELECT COUNT(*) FROM user_alarms")
    long count();
}

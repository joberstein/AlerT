package com.jesseoberstein.alert.data.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;

@Dao
public interface AlarmEndpointDao extends BaseDao<AlarmEndpoint> {

    @Query("SELECT * FROM alarm_endpoints WHERE id == :id")
    List<AlarmEndpoint> get(long id);

    @Query("SELECT * FROM alarm_endpoints WHERE alarm_id == :alarmId")
    List<AlarmEndpoint> getByAlarm(long alarmId);

    @Transaction
    @Query("SELECT e.* FROM alarm_endpoints ae JOIN endpoints e ON ae.endpoint_id = e.id WHERE ae.alarm_id = :alarmId")
    List<Endpoint> getEndpointsByAlarm(long alarmId);

    @Override
    @Query("SELECT * FROM alarm_endpoints")
    List<AlarmEndpoint> getAll();

    @Override
    @Query("SELECT COUNT(*) FROM alarm_endpoints")
    long count();
}

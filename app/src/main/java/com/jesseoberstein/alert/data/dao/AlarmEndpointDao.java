package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.List;

@Dao
public interface AlarmEndpointDao extends BaseDao<AlarmEndpoint> {

    @Query("SELECT * FROM alarm_endpoints WHERE id == :id")
    List<AlarmEndpoint> get(long id);

    @Query("SELECT * FROM alarm_endpoints WHERE alarm_id == :alarmId")
    List<AlarmEndpoint> getByAlarm(long alarmId);

    @Override
    @Query("SELECT * FROM alarm_endpoints")
    List<AlarmEndpoint> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(AlarmEndpoint[] alarm);

    @Override
    @Delete
    void delete(AlarmEndpoint[] alarm);

    @Override
    @Query("SELECT COUNT(*) FROM alarm_endpoints")
    long count();
}

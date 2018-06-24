package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jesseoberstein.alert.models.mbta.Stop;

import java.util.List;

@Dao
public interface StopDao extends BaseDao<Stop> {

    @Query("SELECT * FROM stops WHERE route_id IN (:routeIds)")
    List<Stop> get(String[] routeIds);

    @Query("SELECT * FROM stops WHERE id = :stopId")
    Stop get(String stopId);

    @Override
    @Query("SELECT * FROM stops")
    List<Stop> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Stop[] stops);

    @Override
    @Delete
    void delete(Stop[] stops);

    @Override
    @Query("SELECT COUNT(*) FROM stops")
    long count();
}

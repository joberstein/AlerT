package com.jesseoberstein.alert.data.dao;

import androidx.room.Dao;
import androidx.room.Query;

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
    @Query("SELECT COUNT(*) FROM stops")
    long count();
}

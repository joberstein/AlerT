package com.jesseoberstein.alert.data.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.jesseoberstein.alert.models.mbta.Stop;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface StopDao extends BaseDao<Stop> {

    @Query("SELECT * FROM stops WHERE route_id IN (:routeIds)")
    Single<List<Stop>> get(String[] routeIds);

    @Query("SELECT * FROM stops WHERE id = :stopId AND route_id = :routeId AND direction_id = :directionId")
    Single<Stop> get(String stopId, String routeId, int directionId);

    @Override
    @Query("SELECT * FROM stops")
    List<Stop> getAll();

    @Override
    @Query("SELECT COUNT(*) FROM stops")
    long count();
}

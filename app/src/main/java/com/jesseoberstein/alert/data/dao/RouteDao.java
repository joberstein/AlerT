package com.jesseoberstein.alert.data.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.jesseoberstein.alert.models.mbta.Route;

import java.util.List;

@Dao
public interface RouteDao extends BaseDao<Route> {

    @Query("SELECT * FROM routes WHERE id = :routeId")
    Route get(String routeId);

    @Override
    @Query("SELECT * FROM routes")
    List<Route> getAll();

    @Override
    @Query("SELECT COUNT(*) FROM routes")
    long count();
}

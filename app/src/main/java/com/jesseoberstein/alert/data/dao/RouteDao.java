package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Route[] routes);

    @Override
    @Delete
    void delete(Route[] routes);

    @Override
    @Query("SELECT COUNT(*) FROM routes")
    long count();
}

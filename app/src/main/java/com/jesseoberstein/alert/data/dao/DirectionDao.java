package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;

@Dao
public interface DirectionDao extends BaseDao<Direction> {

    @Query("SELECT * FROM directions WHERE route_id IN (:routeIds) ORDER BY id")
    List<Direction> get(String[] routeIds);

    @Override
    @Query("SELECT * FROM directions")
    List<Direction> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Direction[] directions);

    @Override
    @Delete
    void delete(Direction[] directions);

    @Override
    @Query("SELECT COUNT(*) FROM directions")
    long count();
}

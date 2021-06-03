package com.jesseoberstein.alert.data.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface DirectionDao extends BaseDao<Direction> {

    @Query("SELECT * FROM directions WHERE route_id IN (:routeIds) ORDER BY direction_id")
    List<Direction> get(String[] routeIds);

    @Query("SELECT * FROM directions WHERE id = :directionId AND route_id = :routeId")
    Single<Direction> get(long directionId, String routeId);

    @Override
    @Query("SELECT * FROM directions")
    List<Direction> getAll();

    @Override
    @Query("SELECT COUNT(*) FROM directions")
    long count();
}

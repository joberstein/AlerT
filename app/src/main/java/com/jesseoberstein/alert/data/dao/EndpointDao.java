package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;

@Dao
public interface EndpointDao extends BaseDao<Endpoint> {

    @Query("SELECT * FROM endpoints WHERE route_id = :routeId AND direction_id = :directionId ORDER BY name")
    List<Endpoint> get(String routeId, long directionId);

    @Override
    @Query("SELECT * FROM endpoints")
    List<Endpoint> getAll();

    @Override
    @Query("SELECT COUNT(*) FROM endpoints")
    long count();
}

package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;

@Dao
public interface EndpointDao extends BaseDao<Endpoint> {

    @Query("SELECT * FROM endpoints WHERE route_id = :routeId AND direction_id = :directionId ORDER BY name")
    List<Endpoint> get(String routeId, int directionId);

    @Override
    @Query("SELECT * FROM endpoints")
    List<Endpoint> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Endpoint[] endpoints);

    @Override
    @Delete
    void delete(Endpoint[] endpoints);

    @Override
    @Query("SELECT COUNT(*) FROM endpoints")
    long count();
}

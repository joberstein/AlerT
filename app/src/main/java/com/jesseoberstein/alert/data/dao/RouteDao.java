package com.jesseoberstein.alert.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jesseoberstein.alert.models.mbta.Route;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM routes WHERE id = :routeId")
    Single<Route> get(String routeId);

    @Query("SELECT * FROM routes")
    Single<List<Route>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<List<Long>> insert(Route[] elements);

    @Query("SELECT COUNT(*) FROM routes")
    long count();
}

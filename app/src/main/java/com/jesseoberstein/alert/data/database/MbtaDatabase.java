package com.jesseoberstein.alert.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.jesseoberstein.alert.data.dao.DirectionDao;
import com.jesseoberstein.alert.data.dao.EndpointDao;
import com.jesseoberstein.alert.data.dao.RouteDao;
import com.jesseoberstein.alert.data.dao.StopDao;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;

@Database(entities = {Route.class, Stop.class, Direction.class, Endpoint.class}, version = 1, exportSchema = false)
public abstract class MbtaDatabase extends RoomDatabase {
    private static final String MBTA_DATABASE_NAME = "mbta";
    static final String MBTA_DATABASE_FILENAME = MBTA_DATABASE_NAME + ".db";
    private static MbtaDatabase instance;

    public abstract RouteDao routeDao();
    public abstract StopDao stopDao();
    public abstract DirectionDao directionDao();
    public abstract EndpointDao endpointDao();

    public static MbtaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MbtaDatabase.class, MBTA_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

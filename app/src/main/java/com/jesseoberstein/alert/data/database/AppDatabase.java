package com.jesseoberstein.alert.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jesseoberstein.alert.data.dao.AlarmEndpointDao;
import com.jesseoberstein.alert.data.dao.DirectionDao;
import com.jesseoberstein.alert.data.dao.EndpointDao;
import com.jesseoberstein.alert.data.dao.RouteDao;
import com.jesseoberstein.alert.data.dao.StopDao;
import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;

@Database(
    version = 1,
    entities = {
            Route.class,
            Stop.class,
            Direction.class,
            Endpoint.class,
            UserAlarm.class,
            AlarmEndpoint.class
    })
public abstract class AppDatabase extends RoomDatabase {
    public static final String ALERT_DATABASE_NAME = "alert";
    public abstract RouteDao routeDao();
    public abstract StopDao stopDao();
    public abstract DirectionDao directionDao();
    public abstract EndpointDao endpointDao();
    public abstract UserAlarmDao userAlarmDao();
    public abstract AlarmEndpointDao alarmEndpointDao();
}

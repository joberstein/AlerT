package com.jesseoberstein.alert.data;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;

import java.util.List;

public class StopDao {
    private static StopDao instance;
    private static RuntimeExceptionDao<Stop, String> stopDao;

    private StopDao() {
        stopDao = DatabaseHelper.getStopDao();
    }

    static void init() {
        if (instance == null) {
            instance = new StopDao();
        }
    }

    public static List<Stop> getStopsForRoute(Route route) {
        return stopDao.queryForEq("route_id", route.getId());
    }

    public static void createStopsForRoute(List<Stop> stops, Route route) {
        stopDao.callBatchTasks(() -> {
            stops.forEach(stop -> {
                stop.setRoute(route);
                stopDao.createIfNotExists(stop);
            });
            return null;
        });
    }

    public static long getRecordCount() {
        return stopDao.countOf();
    }
}

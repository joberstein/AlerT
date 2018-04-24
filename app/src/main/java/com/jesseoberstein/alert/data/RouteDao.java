package com.jesseoberstein.alert.data;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jesseoberstein.alert.models.mbta.Route;

import java.util.List;

public class RouteDao {
    private static RouteDao instance;
    private static RuntimeExceptionDao<Route, String> routeDao;

    private RouteDao() {
        routeDao = DatabaseHelper.getRouteDao();
    }

    static void init() {
        if (instance == null) {
            instance = new RouteDao();
        }
    }

    public static List<Route> getRoutes() {
        return routeDao.queryForAll();
    }

    public static void createRoutes(List<Route> routes) {
        routeDao.callBatchTasks(() -> {
            routes.forEach(routeDao::createIfNotExists);
            return null;
        });
    }

    public static long getRecordCount() {
        return routeDao.countOf();
    }
}

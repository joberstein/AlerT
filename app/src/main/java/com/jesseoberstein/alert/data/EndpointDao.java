package com.jesseoberstein.alert.data;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Trip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndpointDao {
    private static EndpointDao instance;
    private static RuntimeExceptionDao<Endpoint, Integer> endpointDao;

    private EndpointDao() {
        endpointDao = DatabaseHelper.getEndpointDao();
    }

    static void init() {
        if (instance == null) {
            instance = new EndpointDao();
        }
    }

    public static List<Endpoint> getEndpointsForRoute(Route route) {
        return endpointDao.queryForEq("route_id", route.getId());
    }

    public static void createEndpointsForRoute(List<Trip> trips, Route route) {
        endpointDao.callBatchTasks(() -> {
            trips.forEach(trip -> {
                int directionId = trip.getDirectionId();
                String directionName = route.getDirectionNames().get(directionId);
                Endpoint endpoint = new Endpoint(trip.getHeadsign(), directionId, directionName, route);

                Map<String, Object> endpointValues = new HashMap<>();
                endpointValues.put("name", endpoint.getName());
                endpointValues.put("direction_id", endpoint.getDirectionId());
                endpointValues.put("direction_name", endpoint.getDirectionName());
                endpointValues.put("route_id", endpoint.getRoute().getId());

                if (endpointDao.queryForFieldValuesArgs(endpointValues).isEmpty()) {
                    endpointDao.create(endpoint);
                }
            });
            return null;
        });
    }

    public static long getRecordCount() {
        return endpointDao.countOf();
    }
}

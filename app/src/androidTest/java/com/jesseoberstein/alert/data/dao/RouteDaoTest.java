package com.jesseoberstein.alert.data.dao;

import android.support.test.runner.AndroidJUnit4;

import com.jesseoberstein.alert.models.mbta.Route;

import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RouteDaoTest extends BaseDaoTest {
    private Route[] testRoutes;

    @Before
    public void setup() {
        this.testRoutes = getTestRoutes();
    }

    @Override
    BaseDao<Route> getDao() {
        return getTestDatabase().routeDao();
    }

    @Override
    Route[] getTestElements() {
        return this.testRoutes;
    }

    private Route[] getTestRoutes() {
        return new Route[]{
            createTestRoute("Orange"),
            createTestRoute("Green-B"),
            createTestRoute("Red")
        };
    }

    private Route createTestRoute(String routeId) {
        Route route = new Route();
        route.setId(routeId);
        return route;
    }
}

package com.jesseoberstein.alert.data.dao;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jesseoberstein.alert.models.mbta.Route;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import io.reactivex.rxjava3.schedulers.Schedulers;

import static org.junit.Assert.assertNull;

@Ignore("No longer a BaseDao")
@RunWith(AndroidJUnit4.class)
public class RouteDaoTest extends BaseDaoTest {
    private RouteDao routeDao;
    private Route[] testRoutes;

    @Before
    public void setup() {
        this.routeDao = getTestDatabase().routeDao();
        this.testRoutes = getTestRoutes();
        this.routeDao.insert(this.testRoutes);
    }

    @After
    public void cleanup() {
//        this.routeDao.delete(this.testRoutes);
    }

    @Override
    BaseDao<Route> getDao() {
//        return getTestDatabase().routeDao();
        return null;
    }

    @Override
    Route[] getTestElements() {
        return this.testRoutes;
    }


    @Test
    public void testGetByRouteId() {
        this.routeDao.get("Red")
                .subscribeOn(Schedulers.computation())
                .subscribe(route -> {
                    assertDaoResults(Collections.singletonList(route), Collections.singletonList(2));
                });
    }

    @Test
    public void testGetByRouteId_invalid() {
        assertNull(this.routeDao.get("Fake"));
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

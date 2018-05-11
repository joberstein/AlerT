package com.jesseoberstein.alert.data.dao;

import android.support.test.runner.AndroidJUnit4;

import com.jesseoberstein.alert.models.mbta.Stop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class StopDaoTest extends BaseDaoTest {
    private StopDao stopDao;
    private Stop[] testStops;

    @Before
    public void setup() {
        this.stopDao = getTestDatabase().stopDao();
        this.testStops = getTestStops();
        this.stopDao.insert(this.testStops);
    }

    @After
    public void cleanup() {
        this.stopDao.delete(this.testStops);
    }

    @Override
    BaseDao<Stop> getDao() {
        return this.stopDao;
    }

    @Override
    Stop[] getTestElements() {
        return this.testStops;
    }

    @Test
    public void testGetStopByRouteId_noIds() {
        List<Stop> stops = stopDao.get(new String[]{});
        assertDaoResults(stops, Collections.emptyList());
    }

    @Test
    public void testGetStopByRouteId_invalidId() {
        List<Stop> stops = stopDao.get(new String[]{"Purple"});
        assertDaoResults(stops, Collections.emptyList());
    }

    @Test
    public void testGetStopByRouteId_singleId() {
        List<Stop> stops = stopDao.get(new String[]{"Green-B"});
        assertDaoResults(stops, Collections.singletonList(1));
    }

    @Test
    public void testGetStopByRouteId_multipleIds() {
        List<Stop> stops = stopDao.get(new String[]{"Red", "Green-B"});
        assertDaoResults(stops, Arrays.asList(1, 2));
    }

    private Stop[] getTestStops() {
        return new Stop[]{
            createTestStop("Orange", "Oak Grove", "place-oakgr"),
            createTestStop("Green-B", "Boston College", "place-boscl"),
            createTestStop("Red", "South Station", "place-sostn")
        };
    }

    private Stop createTestStop(String routeId, String stopName, String stopId) {
        Stop stop = new Stop();
        stop.setId(stopId);
        stop.setName(stopName);
        stop.setRouteId(routeId);
        return stop;
    }
}

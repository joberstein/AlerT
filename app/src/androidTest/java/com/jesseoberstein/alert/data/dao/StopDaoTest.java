package com.jesseoberstein.alert.data.dao;

import android.support.test.runner.AndroidJUnit4;

import com.jesseoberstein.alert.models.mbta.Stop;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

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
        assertThat(stopDao.get(new String[]{}), hasSize(0));
    }

    @Test
    public void testGetStopByRouteId_invalidId() {
        assertThat(stopDao.get(new String[]{"Purple"}), hasSize(0));
    }

    @Test
    public void testGetStopByRouteId_singleId() {
        String[] routeId = new String[]{"Green-B"};
        assertThat(stopDao.get(routeId), contains(getTestElements()[1]));
    }

    @Test
    public void testGetStopByRouteId_multipleIds() {
        String[] routeIds = new String[]{"Red", "Green-B"};
        assertThat(stopDao.get(routeIds), contains(getTestElements()[1], getTestElements()[2]));
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

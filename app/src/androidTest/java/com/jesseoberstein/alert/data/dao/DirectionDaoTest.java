package com.jesseoberstein.alert.data.dao;

import android.support.test.runner.AndroidJUnit4;

import com.jesseoberstein.alert.models.mbta.Direction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DirectionDaoTest extends BaseDaoTest {
    private DirectionDao directionDao;
    private Direction[] testDirections;

    @Before
    public void setup() {
        this.directionDao = getTestDatabase().directionDao();
        this.testDirections = getTestDirections();
        this.directionDao.insert(this.testDirections);
    }

    @After
    public void cleanup() {
        this.directionDao.delete(this.testDirections);
    }

    @Override
    BaseDao<Direction> getDao() {
        return this.directionDao;
    }

    @Override
    Direction[] getTestElements() {
        return this.testDirections;
    }

    @Test
    public void testGetDirectionByRouteId_noIds() {
        List<Direction> directions = directionDao.get(new String[]{});
        assertDaoResults(directions, Collections.emptyList());
    }

    @Test
    public void testGetDirectionByRouteId_invalidId() {
        List<Direction> directions = directionDao.get(new String[]{"Purple"});
        assertDaoResults(directions, Collections.emptyList());
    }

    @Test
    public void testGetDirectionByRouteId_singleId() {
        List<Direction> directions = directionDao.get(new String[]{"Orange"});
        assertDaoResults(directions, Arrays.asList(0, 1));
    }

    @Test
    public void testGetDirectionByRouteId_multipleIds() {
        List<Direction> directions = directionDao.get(new String[]{"Orange", "Green-B"});
        assertDaoResults(directions, Arrays.asList(0, 1, 2));
    }

    private Direction[] getTestDirections() {
        return new Direction[]{
            new Direction(0, "Northbound", "Orange"),
            new Direction(1, "Southbound", "Orange"),
            new Direction(0, "Eastbound", "Green-B"),
            new Direction(1, "Northbound", "Red")
        };
    }
}

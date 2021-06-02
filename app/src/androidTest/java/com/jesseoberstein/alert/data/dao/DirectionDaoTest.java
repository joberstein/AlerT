package com.jesseoberstein.alert.data.dao;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jesseoberstein.alert.models.mbta.Direction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import io.reactivex.rxjava3.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void testGetByRouteId_noIds() {
        List<Direction> directions = directionDao.get(new String[]{});
        assertDaoResults(directions, Collections.emptyList());
    }

    @Test
    public void testGetByRouteId_invalidId() {
        List<Direction> directions = directionDao.get(new String[]{"Purple"});
        assertDaoResults(directions, Collections.emptyList());
    }

    @Test
    public void testGetByRouteId_singleId() {
        List<Direction> directions = directionDao.get(new String[]{"Orange"});
        assertDaoResults(directions, Arrays.asList(0, 1));
    }

    @Test
    public void testGetByRouteId_multipleIds() {
        List<Direction> directions = directionDao.get(new String[]{"Orange", "Green-B"});
        assertDaoResults(directions, Arrays.asList(0, 1, 2));
    }

    @Test
    public void testGetByRouteIdAndDirectionId() {
        directionDao.get(3, "Green-B")
            .subscribeOn(Schedulers.computation())
            .subscribe(direction -> assertEquals(direction, getTestDirections()[2]));
    }

    @Test
    public void testGetByRouteIdAndDirectionId_invalid() {
        assertNull(directionDao.get(0, "Fake"));
        assertNull(directionDao.get(2, "Green-B"));
    }

    private Direction[] getTestDirections() {
        Direction[] directions = new Direction[]{
            new Direction(0, "Northbound", "Orange"),
            new Direction(1, "Southbound", "Orange"),
            new Direction(0, "Eastbound", "Green-B"),
            new Direction(1, "Northbound", "Red")
        };

        IntStream.range(0, directions.length).forEach(i -> directions[i].setId(i + 1));
        return directions;

    }
}

package com.jesseoberstein.alert.data.dao;

import android.support.test.runner.AndroidJUnit4;

import com.jesseoberstein.alert.models.AlarmEndpoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class AlarmEndpointDaoTest extends BaseDaoTest {
    private AlarmEndpointDao alarmEndpointDao;
    private AlarmEndpoint[] testAlarmEndpoints;

    @Before
    public void setup() {
        this.alarmEndpointDao = getTestDatabase().alarmEndpointDao();
        this.testAlarmEndpoints = getTestAlarmEndpoints();
        List<Long> insertedIds = this.alarmEndpointDao.insert(this.testAlarmEndpoints);
        IntStream.range(0, insertedIds.size()).forEach(i -> this.testAlarmEndpoints[i].setId(insertedIds.get(i)));
    }

    @After
    public void cleanup() {
        this.alarmEndpointDao.delete(this.testAlarmEndpoints);
    }

    @Override
    BaseDao<AlarmEndpoint> getDao() {
        return this.alarmEndpointDao;
    }

    @Override
    AlarmEndpoint[] getTestElements() {
        return this.testAlarmEndpoints;
    }

    @Test
    public void testGetAlarmEndpointById() {
        assertDaoResults(alarmEndpointDao.get(4), Arrays.asList(3));
    }

    @Test
    public void testGetAlarmEndpointsByAlarmId_invalidId() {
        List<AlarmEndpoint> alarmEndpoints = alarmEndpointDao.getByAlarm(3);
        assertDaoResults(alarmEndpoints, Collections.emptyList());
    }

    @Test
    public void testGetAlarmEndpointsByAlarmId_singleId() {
        List<AlarmEndpoint> alarmEndpoints = alarmEndpointDao.getByAlarm(1);
        assertDaoResults(alarmEndpoints, Arrays.asList(0, 1, 2));
    }

    private AlarmEndpoint[] getTestAlarmEndpoints() {
        return new AlarmEndpoint[]{
            new AlarmEndpoint(1, 1),
            new AlarmEndpoint(1, 2),
            new AlarmEndpoint(1, 3),
            new AlarmEndpoint(2, 1)
        };
    }
}

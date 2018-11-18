package com.jesseoberstein.alert.data.dao;

import android.support.test.runner.AndroidJUnit4;

import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Route;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class UserAlarmDaoTest extends BaseDaoTest {
    private UserAlarmDao userAlarmDao;
    private UserAlarm[] testAlarms;

    @Before
    public void setup() {
        this.userAlarmDao = getTestDatabase().userAlarmDao();
        this.testAlarms = getTestAlarms();
        this.userAlarmDao.insert(this.testAlarms);
    }

    @After
    public void cleanup() {
        this.userAlarmDao.delete(this.testAlarms);
    }

    @Override
    BaseDao<UserAlarm> getDao() {
        return this.userAlarmDao;
    }

    @Override
    UserAlarm[] getTestElements() {
        return this.testAlarms;
    }

    @Test
    public void testGetDirectionByRouteId_invalidId() {
        assertNull(userAlarmDao.get(7));
    }

    @Test
    public void testGetDirectionByRouteId_singleId() {
        UserAlarm userAlarm = userAlarmDao.get(5L);
        assertDaoResults(Collections.singletonList(userAlarm), Collections.singletonList(0));
    }

    private UserAlarm[] getTestAlarms() {
        UserAlarm testAlarm = new UserAlarm();
        testAlarm.setNickname("test");
        testAlarm.setDuration(15);
        testAlarm.setId(5);
        testAlarm.setActive(true);
        testAlarm.setHour(3);
        testAlarm.setMinutes(46);
        return new UserAlarm[]{testAlarm};
    }
}

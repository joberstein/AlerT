package com.jesseoberstein.alert.model;

import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserAlarmWithRelationsTest {
    private UserAlarmWithRelations testAlarmWithRelations;
    private UserAlarm validAlarm;

    @Before
    public void setup() {
        Route testRoute = new Route();
        testRoute.setId("Orange");
        testRoute.setLongName("Orange Line");

        Stop testStop = new Stop("Orange", 0);
        testStop.setId("Haymarket");

        Direction testDirection = new Direction(0, "Northbound", "Orange");
        testDirection.setId(1);

        validAlarm = UserAlarm.builder()
                .routeId("1")
                .stopId("2")
                .directionId(1L)
                .build();

        testAlarmWithRelations = new UserAlarmWithRelations();
        testAlarmWithRelations.setAlarm(validAlarm);
        testAlarmWithRelations.setRoute(testRoute);
        testAlarmWithRelations.setStop(testStop);
        testAlarmWithRelations.setDirection(testDirection);
        testAlarmWithRelations.setEndpoints(Collections.singletonList(new Endpoint("Forest Hills", 1, "Orange")));
    }

    @Test
    public void testNoArgConstructor() {
        testAlarmWithRelations = new UserAlarmWithRelations();
        assertEquals(new UserAlarm(), testAlarmWithRelations.getAlarm());
        assertNull(testAlarmWithRelations.getRoute());
        assertNull(testAlarmWithRelations.getDirection());
        assertNull(testAlarmWithRelations.getStop());
        assertEquals(Collections.emptyList(), testAlarmWithRelations.getEndpoints());
    }

    @Test
    public void testValidAlarm() {
        assertTrue(testAlarmWithRelations.isValid());
    }


    @Test
    public void testInValidAlarmData() {
        UserAlarm validAlarm = UserAlarm.builder()
            .routeId("1")
            .stopId("2")
            .directionId(1L)
            .build();

        testAlarmWithRelations.setAlarm(validAlarm);
        assertFalse(testAlarmWithRelations.isValid());
    }

    @Test
    public void testValidAlarmEndpoints() {
        testAlarmWithRelations.setEndpoints(null);
        assertFalse(testAlarmWithRelations.isValid());

        testAlarmWithRelations.setEndpoints(Collections.emptyList());
        assertFalse(testAlarmWithRelations.isValid());
    }
}

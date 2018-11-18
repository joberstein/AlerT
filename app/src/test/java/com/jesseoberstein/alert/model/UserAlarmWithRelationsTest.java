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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAlarmWithRelationsTest {
    private UserAlarmWithRelations testAlarmWithRelations;
    @Mock private UserAlarm testAlarm;

    @Before
    public void setup() {
        Route testRoute = new Route();
        testRoute.setId("Orange");
        testRoute.setLongName("Orange Line");

        Stop testStop = new Stop();
        testStop.setId("Haymarket");
        testStop.setRouteId("Orange");

        Direction testDirection = new Direction(0, "Northbound", "Orange");
        testDirection.setId(1);

        when(testAlarm.isValid()).thenReturn(true);
        doNothing().when(testAlarm).setRouteId(anyString());
        doNothing().when(testAlarm).setStopId(anyString());
        doNothing().when(testAlarm).setDirectionId(anyLong());

        testAlarmWithRelations = new UserAlarmWithRelations();
        testAlarmWithRelations.setAlarm(testAlarm);
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
    public void testSingleArgConstructor() {
        UserAlarm testAlarm = new UserAlarm();
        testAlarm.setNickname("nickname");
        testAlarm.setRouteId("routeId");
        testAlarmWithRelations.setAlarm(testAlarm);
        assertEquals(testAlarmWithRelations, new UserAlarmWithRelations(testAlarmWithRelations));
    }

    @Test
    public void testValidAlarm() {
        assertTrue(testAlarmWithRelations.isValid());
    }


    @Test
    public void testValidAlarmData() {
        when(testAlarm.isValid()).thenReturn(false);
        when(testAlarm.getErrors()).thenReturn(Arrays.asList("invalid"));
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

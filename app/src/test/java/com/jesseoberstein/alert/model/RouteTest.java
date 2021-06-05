package com.jesseoberstein.alert.model;

import com.jesseoberstein.alert.models.mbta.Route;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static junit.framework.Assert.assertEquals;

public class RouteTest {
    private Route testRoute;

    @Before
    public void setup() {
        testRoute = new Route();
    }

    @Test
    public void testGetDirectionName() {
        List<String> directionNames = Arrays.asList("North", "South", "East", "West");
        testRoute.setDirectionNames(directionNames);
        IntStream.range(0, directionNames.size()).forEach(i ->
            assertEquals(directionNames.get(i), testRoute.getDirectionNames().get(i)));
    }

    @Test
    @Ignore("Do not need to test retrieving from an arraylist")
    public void testGetDirectionName_invalidId() {
        testRoute.setDirectionNames(Collections.singletonList("test"));
        assertEquals("", testRoute.getDirectionNames().get(-1));
        assertEquals("", testRoute.getDirectionNames().get(1));
    }
}

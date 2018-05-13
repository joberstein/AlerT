package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.models.mbta.Endpoint;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class EndpointsUtilsTest {
    private List<Endpoint> testEndpoints;

    @Before
    public void setup() {
        testEndpoints = Arrays.asList(
            new Endpoint("Forest Hills", 0, "Orange"),
            new Endpoint("Oak Grove", 1, "Orange"),
            new Endpoint("Ashmont", 0, "Red"),
            new Endpoint("Braintree", 0, "Red"),
            new Endpoint("Alewife", 1, "Red")
        );
    }

    @Test
    public void sortsEndpointList() {
        List<Endpoint> sortedTestEndpoints = Arrays.asList(
            new Endpoint("Alewife", 1, "Red"),
            new Endpoint("Ashmont", 0, "Red"),
            new Endpoint("Braintree", 0, "Red"),
            new Endpoint("Forest Hills", 0, "Orange"),
            new Endpoint("Oak Grove", 1, "Orange")
        );

        assertEquals(sortedTestEndpoints, EndpointsUtils.sort(testEndpoints));
    }

    @Test
    public void convertsEndpointListToBooleanArray() {
        List<Endpoint> selectedEndpoints = Arrays.asList(
            new Endpoint("Oak Grove", 1, "Orange"),
            new Endpoint("Ashmont", 0, "Red"),
            new Endpoint("Alewife", 1, "Red")
        );

        boolean[] expectedArray = new boolean[]{false, true, true, false, true};
        boolean[] actualArray = EndpointsUtils.toBooleanArray(testEndpoints, selectedEndpoints);
        assertTrue(Arrays.equals(expectedArray, actualArray));
    }

    @Test
    public void convertsEndpointListToStringArray() {
        String[] expectedArray = new String[]{"Forest Hills", "Oak Grove", "Ashmont", "Braintree", "Alewife"};
        String[] actualArray = EndpointsUtils.toStringArray(testEndpoints);
        assertTrue(Arrays.equals(expectedArray, actualArray));
    }
}
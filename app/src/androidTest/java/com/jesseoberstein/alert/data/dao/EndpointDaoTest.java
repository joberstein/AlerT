package com.jesseoberstein.alert.data.dao;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jesseoberstein.alert.models.mbta.Endpoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(AndroidJUnit4.class)
public class EndpointDaoTest extends BaseDaoTest {
    private EndpointDao endpointDao;
    private Endpoint[] testEndpoints;

    @Before
    public void setup() {
        this.endpointDao = getTestDatabase().endpointDao();
        this.testEndpoints = getTestEndpoints();
        this.endpointDao.insert(this.testEndpoints);
    }

    @After
    public void cleanup() {
        this.endpointDao.delete(this.testEndpoints);
    }

    @Override
    BaseDao<Endpoint> getDao() {
        return this.endpointDao;
    }

    @Override
    Endpoint[] getTestElements() {
        return this.testEndpoints;
    }

    @Test
    public void testGetEndpointByRouteAndDirectionIds_invalidRoute() {
        List<Endpoint> endpoints = endpointDao.get("Purple", 0);
        assertDaoResults(endpoints, Collections.emptyList());
    }

    @Test
    public void testGetEndpointByRouteAndDirectionIds_invalidDirection() {
        List<Endpoint> endpoints = endpointDao.get("Orange", 7);
        assertDaoResults(endpoints, Collections.emptyList());
    }

    @Test
    public void testGetEndpointByRouteAndDirectionIds_() {
        List<Endpoint> endpoints = endpointDao.get("Orange", 1);
        assertDaoResults(endpoints, Collections.singletonList(1));
    }

    @Test
    public void testGetEndpointByRouteId_multipleIds() {
        List<Endpoint> endpoints = endpointDao.get("Red", 0);
        assertDaoResults(endpoints, Arrays.asList(2, 3));
    }

    private Endpoint[] getTestEndpoints() {
        Endpoint[] endpoints =  new Endpoint[]{
            new Endpoint("Forest Hills", 0, "Orange"),
            new Endpoint("Oak Grove", 1, "Orange"),
            new Endpoint("Ashmont", 0, "Red"),
            new Endpoint("Braintree", 0, "Red"),
            new Endpoint("Alewife", 1, "Red")
        };

        IntStream.range(0, endpoints.length).forEach(i -> endpoints[i].setId(i + 1));
        return endpoints;
    }
}

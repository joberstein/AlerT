package com.jesseoberstein.alert.data.dao;

import android.support.test.runner.AndroidJUnit4;

import com.jesseoberstein.alert.models.mbta.Endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

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
        assertThat(endpointDao.get("Purple", 0), hasSize(0));
    }

    @Test
    public void testGetEndpointByRouteAndDirectionIds_invalidDirection() {
        assertThat(endpointDao.get("Orange", 7), hasSize(0));
    }

    @Test
    public void testGetEndpointByRouteAndDirectionIds_() {
        assertThat(endpointDao.get("Orange", 1), contains(getTestElements()[1]));
    }

    @Test
    public void testGetEndpointByRouteId_multipleIds() {
        assertThat(endpointDao.get("Red", 0), contains(getTestElements()[2], getTestElements()[3]));
    }

    private Endpoint[] getTestEndpoints() {
        Endpoint[] endpoints =  new Endpoint[]{
            createTestEndpoint("Orange", "Forest Hills", 0),
            createTestEndpoint("Orange", "Oak Grove", 1),
            createTestEndpoint("Red", "Ashmont", 0),
            createTestEndpoint("Red", "Braintree", 0),
            createTestEndpoint("Red", "Alewife", 1)
        };

        IntStream.range(0, endpoints.length).forEach(i -> endpoints[i].setId(i + 1));
        return endpoints;
    }

    private Endpoint createTestEndpoint(String routeId, String endpointName, int directionId) {
        Endpoint endpoint = new Endpoint();
        endpoint.setName(endpointName);
        endpoint.setDirectionId(directionId);
        endpoint.setRouteId(routeId);
        return endpoint;
    }
}

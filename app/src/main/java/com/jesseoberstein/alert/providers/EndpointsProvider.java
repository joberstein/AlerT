package com.jesseoberstein.alert.providers;

import android.content.res.AssetManager;

import com.jesseoberstein.mbta.model.CompleteRoute;
import com.jesseoberstein.mbta.model.Endpoint;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.utils.ResponseParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.Constants.MBTA_DATA_DATE;

/**
 * Provides access to MBTA route data in many different forms.
 */
public class EndpointsProvider {
    private List<Endpoint> endpoints;

    public EndpointsProvider(AssetManager assetManager, String routeId) {
        serializeEndpoints(assetManager, routeId);
    }

    public List<Endpoint> getEndpoints() {
        return this.endpoints;
    }

    public List<String> getEndpointDirections() {
        return this.endpoints.stream().map(Endpoint::getDirectionName).collect(Collectors.toList());
    }

    public List<Endpoint> getEndpointsForDirection(int directionId) {
        return this.endpoints.stream()
                .filter(endpoint -> endpoint.getDirectionId() == directionId)
                .collect(Collectors.toList());
    }

    public List<Endpoint> getEndpointsForDirection(String directionName) {
        return this.endpoints.stream()
                .filter(endpoint -> endpoint.getDirectionName().equals(directionName))
                .collect(Collectors.toList());
    }

    public List<String> getEndpointNames(List<Endpoint> endpoints) {
        return endpoints.stream().map(Endpoint::getName).collect(Collectors.toList());
    }

    private void serializeEndpoints(AssetManager assetManager, String routeId) {
        if (endpoints != null) {
            return;
        }

        try {
            InputStream inputStream = assetManager.open( MBTA_DATA_DATE +"/endpoints.json");
            endpoints = ResponseParser.parseJSONArray(inputStream, Endpoint.class).stream()
                    .filter(endpoint -> endpoint.getRouteId().equals(routeId))
                    .distinct()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            endpoints = Collections.emptyList();
        }
    }
}

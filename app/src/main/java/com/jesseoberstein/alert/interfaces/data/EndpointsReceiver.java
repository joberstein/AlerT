package com.jesseoberstein.alert.interfaces.data;

import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;

public interface EndpointsReceiver {

    List<Endpoint> getEndpointList();

    void onReceiveEndpoints(List<Endpoint> endpoints);
}

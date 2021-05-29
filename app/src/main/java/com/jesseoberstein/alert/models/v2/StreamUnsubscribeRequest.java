package com.jesseoberstein.alert.models.v2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class StreamUnsubscribeRequest {
    private String routeId;
    private String stopId;
    private String clientId;
}

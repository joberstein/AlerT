package com.jesseoberstein.alert.models.v2;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder(toBuilder = true)
public class Connection {
    private String routeId;
    private String stopId;
}

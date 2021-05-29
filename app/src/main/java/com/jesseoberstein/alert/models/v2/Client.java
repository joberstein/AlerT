package com.jesseoberstein.alert.models.v2;

import java.time.Duration;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Client {
    private String id;
    private String token;
    private int directionId;
    private Duration ttl;
}

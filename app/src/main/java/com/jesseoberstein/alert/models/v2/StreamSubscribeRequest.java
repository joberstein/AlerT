package com.jesseoberstein.alert.models.v2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class StreamSubscribeRequest {
    private Connection connection;
    private Client client;
}

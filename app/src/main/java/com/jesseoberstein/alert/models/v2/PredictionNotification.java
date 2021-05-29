package com.jesseoberstein.alert.models.v2;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PredictionNotification {
    private String route;
    private String stop;
    private String direction;
    private List<String> displayTimes;
    private LocalDateTime created;
}

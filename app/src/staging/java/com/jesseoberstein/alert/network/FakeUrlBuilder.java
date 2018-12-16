package com.jesseoberstein.alert.network;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeUrlBuilder extends UrlBuilder {

    @Override
    public String buildPredictionRequestUrl(String routeId, String stopId, int directionId) {
        return Stream.of(routeId, stopId, Integer.toString(directionId))
                .collect(Collectors.joining("-"))
                .replaceAll(" ", "-")
                .toLowerCase() + ".txt";
    }
}

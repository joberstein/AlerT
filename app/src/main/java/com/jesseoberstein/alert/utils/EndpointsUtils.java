package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class EndpointsUtils {

    public static List<Endpoint> sort(List<Endpoint> endpoints) {
        return endpoints.stream().sorted(comparing(Endpoint::getName)).collect(toList());
    }

    public static String[] toStringArray(List<Endpoint> endpoints) {
        return endpoints.stream().map(Endpoint::getName).toArray(String[]::new);
    }

    public static boolean[] toBooleanArray(List<Endpoint> endpoints, List<Endpoint> selectedEndpoints) {
        boolean[] convertedEndpoints = new boolean[endpoints.size()];
        IntStream.range(0, convertedEndpoints.length).forEach(i -> {
            boolean isSelected = selectedEndpoints.contains(endpoints.get(i));
            convertedEndpoints[i] = isSelected;
        });
        return convertedEndpoints;
    }
}

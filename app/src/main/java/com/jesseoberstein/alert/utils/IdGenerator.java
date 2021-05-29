package com.jesseoberstein.alert.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private static AtomicInteger id;
    private static Map<String, Integer> ids;

    public static void initialize() {
        if (ids == null) {
            ids = new HashMap<>();
        }

        if (id == null) {
            id = new AtomicInteger(1);
        }
    }

    public static int get(String routeId, String stopId, String directionId) {
        String key = buildKey(routeId, stopId, directionId);
        return ids.computeIfAbsent(key, k -> id.getAndIncrement());
    }

    private static String buildKey(String routeId, String stopId, String directionId) {
        return String.join(":", routeId, stopId, directionId);
    }
}

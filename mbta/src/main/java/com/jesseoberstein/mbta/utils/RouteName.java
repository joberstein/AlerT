package com.jesseoberstein.mbta.utils;

import java.util.Arrays;

public enum RouteName {
    BLUE("Blue Line"),
    GREEN("Green Line"),
    ORANGE("Orange Line"),
    RED("Red Line"),
    SILVER("Silver Line"),
    MATTAPAN("Mattapan Trolley");

    private final String fullName;

    RouteName(String fullName) {
        this.fullName = fullName;
    }

    public static RouteName getEnum(String name) throws Throwable {
        return Arrays.stream(RouteName.values())
                .filter(val -> val.toString().equals(name))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return this.fullName;
    }
}

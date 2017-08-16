package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Routes {
    @JsonProperty("mode")
    List<Mode> modes;

    public Routes() {}

    public Routes(List<Mode> modes) {
        this.modes = modes;
    }

    public List<Mode> getModes() {
        return modes;
    }

    public void setModes(List<Mode> modes) {
        this.modes = modes;
    }

    @Override
    public String toString() {
        return "\n" +
                "{" + "\n\t" +
                "directions: " + modes + "\n" +
                "}";
    }
}

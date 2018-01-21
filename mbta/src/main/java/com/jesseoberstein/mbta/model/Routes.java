package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Routes {
    @JsonProperty("mode")
    List<Mode> modes;

    @JsonProperty("alert_headers")
    private List<Alert> alerts;

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

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    @Override
    public String toString() {
        return "\n" +
                "{" + "\n\t" +
                "modes: " + modes + "\n" +
                "alerts: " + alerts + "\n" +
                "}";
    }
}

package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

class Alert {

    @JsonProperty("alert_id")
    private long id;

    @JsonProperty("header_text")
    private String message;

    @JsonProperty("effect_name")
    private String effectType;

    public Alert() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    @Override
    public String toString() {
        return "Alert{" + "\n\t" +
                "id=" + id + "\n\t" +
                "message='" + message + "\n\t" +
                "effectType='" + effectType + "\n" +
                '}';
    }
}

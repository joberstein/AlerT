package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

import java.util.Date;

import static com.jesseoberstein.mbta.utils.ResponseParser.formatZonedTime;

@Type("prediction")
public class Prediction extends BaseResource implements MbtaDataType {

    @JsonProperty("track")
    private String track;

    @JsonProperty("stop_sequence")
    private int stopSequence;

    @JsonProperty("status")
    private String status;

    @JsonProperty("schedule_relationship")
    private String scheduleRelationship;

    @JsonProperty("direction_id")
    private int directionId;

    @JsonProperty("departure_time")
    private Date departureTime;

    @JsonProperty("arrival_time")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddThh:mm:ssZ")
    private Date arrivalTime;

    @Relationship("trip")
    private Trip trip;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScheduleRelationship() {
        return scheduleRelationship;
    }

    public void setScheduleRelationship(String scheduleRelationship) {
        this.scheduleRelationship = scheduleRelationship;
    }

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public String toString() {
        return "\n" +
                "Prediction {" +
                super.toString() +
                "track='" + track + '\'' +
                ", stopSequence=" + stopSequence +
                ", status='" + status + '\'' +
                ", scheduleRelationship='" + scheduleRelationship + '\'' +
                ", directionId=" + directionId +
                ", departureTime=" + formatZonedTime(departureTime) +
                ", arrivalTime=" + formatZonedTime(arrivalTime) +
                ", trip=" + trip +
                '}';
    }
}

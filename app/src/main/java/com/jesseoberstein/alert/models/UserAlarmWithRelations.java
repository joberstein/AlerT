package com.jesseoberstein.alert.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.jesseoberstein.alert.BR;
import com.jesseoberstein.alert.models.mbta.BaseResource;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.Constants;
import com.jesseoberstein.alert.validation.Validatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

public class UserAlarmWithRelations extends BaseObservable implements Serializable, Validatable {

    @Embedded
    private UserAlarm alarm;

    @Relation(entity = Route.class, entityColumn = "id", parentColumn = "route_id")
    private Set<Route> routes = Collections.emptySet();

    @Relation(entity = Stop.class, entityColumn = "id", parentColumn = "stop_id")
    private Set<Stop> stops = Collections.emptySet();

    @Relation(entity = Direction.class, entityColumn = "id", parentColumn = "direction_id")
    private Set<Direction> directions = Collections.emptySet();

    @Relation(entity = AlarmEndpoint.class, entityColumn = "alarm_id", parentColumn = "id")
    private List<AlarmEndpoint> alarmEndpoints = Collections.emptyList();

    @Ignore
    private List<Endpoint> endpoints = Collections.emptyList();

    @Ignore
    private List<String> errors = new ArrayList<>();

    public UserAlarmWithRelations() {
        setAlarm(new UserAlarm());
        setRoute(null);
        setDirection(null);
        setStop(null);
        setAlarmEndpoints(new ArrayList<>());
        setEndpoints(new ArrayList<>());
    }

    public UserAlarmWithRelations(UserAlarmWithRelations alarmWithRelations) {
        setAlarm(new UserAlarm(alarmWithRelations.getAlarm()));
        setRoute(alarmWithRelations.getRoute());
        setDirection(alarmWithRelations.getDirection());
        setStop(alarmWithRelations.getStop());
        setAlarmEndpoints(alarmWithRelations.getAlarmEndpoints());
        setEndpoints(alarmWithRelations.getEndpoints());
    }

    @Bindable
    public UserAlarm getAlarm() {
        return alarm;
    }

    public void setAlarm(UserAlarm alarm) {
        this.alarm = alarm;
        notifyPropertyChanged(BR.alarm);
    }

    @Bindable
    public Route getRoute() {
        return this.getItemInSet(routes);
    }

    public void setRoute(Route route) {
        this.routes = this.getSetOfSingleItem(route);
        String routeId = Optional.ofNullable(route).map(BaseResource::getId).orElse("");
        this.alarm.setRouteId(routeId);
        notifyPropertyChanged(BR.route);
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<Route> routes) {
        this.routes = routes;
    }

    @Bindable
    public Direction getDirection() {
        return this.getItemInSet(directions);
    }

    public void setDirection(Direction direction) {
        this.directions = this.getSetOfSingleItem(direction);
        long directionId = Optional.ofNullable(direction).map(Direction::getId).orElse(-1L);
        this.alarm.setDirectionId(directionId);
        notifyPropertyChanged(BR.direction);
    }

    public Set<Direction> getDirections() {
        return directions;
    }

    public void setDirections(Set<Direction> directions) {
        this.directions = directions;
    }

    @Bindable
    public Stop getStop() {
        return this.getItemInSet(stops);
    }

    public void setStop(Stop stop) {
        this.stops = this.getSetOfSingleItem(stop);
        String stopId = Optional.ofNullable(stop).map(Stop::getId).orElse("");
        this.alarm.setStopId(stopId);
        notifyPropertyChanged(BR.stop);
    }

    public Set<Stop> getStops() {
        return stops;
    }

    public void setStops(Set<Stop> stops) {
        this.stops = stops;
    }

    public List<AlarmEndpoint> getAlarmEndpoints() {
        return alarmEndpoints;
    }

    public void setAlarmEndpoints(List<AlarmEndpoint> alarmEndpoints) {
        this.alarmEndpoints = alarmEndpoints;
    }

    @Bindable
    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
        notifyPropertyChanged(BR.endpoints);
    }

    private <T> T getItemInSet(Set<T> items) {
        return items.stream().findAny().orElse(null);
    }

    private <T> Set<T> getSetOfSingleItem(T item) {
        return Optional.ofNullable(item).isPresent() ?
                new HashSet<>(Collections.singletonList(item)) :
                Collections.emptySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return alarm.equals(((UserAlarmWithRelations) o).alarm);
    }

    @Override
    public String toString() {
        return this.alarm.toString();
    }

    @Override
    public int hashCode() {
        return alarm.hashCode();
    }

    @Override
    public boolean isValid() {
        this.errors.clear();

        if (!this.alarm.isValid()) {
            this.errors.addAll(this.alarm.getErrors());
        }

        if (isNull(this.endpoints) || this.endpoints.isEmpty()) {
            this.errors.add(Constants.ENDPOINTS);
        }

        return this.errors.isEmpty();
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }
}
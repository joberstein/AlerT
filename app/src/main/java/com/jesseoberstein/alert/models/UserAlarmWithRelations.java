package com.jesseoberstein.alert.models;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.With;

import static java.util.Objects.isNull;

@With
@Data
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class UserAlarmWithRelations implements Serializable, Validatable {

    @Embedded
    private UserAlarm alarm;

    @Builder.Default
    @Relation(entity = Route.class, entityColumn = "id", parentColumn = "route_id")
    private Set<Route> routes = Collections.emptySet();

    @Builder.Default
    @Relation(entity = Stop.class, entityColumn = "id", parentColumn = "stop_id")
    private Set<Stop> stops = Collections.emptySet();

    @Builder.Default
    @Relation(entity = Direction.class, entityColumn = "id", parentColumn = "direction_id")
    private Set<Direction> directions = Collections.emptySet();

    @Builder.Default
    @Relation(entity = AlarmEndpoint.class, entityColumn = "alarm_id", parentColumn = "id")
    private List<AlarmEndpoint> alarmEndpoints = Collections.emptyList();

    @Ignore
    @Builder.Default
    private List<Endpoint> endpoints = new ArrayList<>();

    @Ignore
    @Builder.Default
    private List<String> errors = new ArrayList<>();

    public UserAlarmWithRelations() {
        setAlarm(new UserAlarm());
        setRoute(null);
        setDirection(null);
        setStop(null);
    }

    public Route getRoute() {
        return this.getItemInSet(routes);
    }

    public void setRoute(Route route) {
        this.routes = this.getSetOfSingleItem(route);
        String routeId = Optional.ofNullable(route).map(BaseResource::getId).orElse("");
        this.alarm.setRouteId(routeId);
    }

    public Direction getDirection() {
        return this.getItemInSet(directions);
    }

    public void setDirection(Direction direction) {
        this.directions = this.getSetOfSingleItem(direction);
        long directionId = Optional.ofNullable(direction).map(Direction::getId).orElse(-1L);
        this.alarm.setDirectionId(directionId);
    }

    public Stop getStop() {
        return this.getItemInSet(stops);
    }

    public void setStop(Stop stop) {
        this.stops = this.getSetOfSingleItem(stop);
        String stopId = Optional.ofNullable(stop).map(Stop::getId).orElse("");
        this.alarm.setStopId(stopId);
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
}
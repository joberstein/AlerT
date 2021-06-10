package com.jesseoberstein.alert.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.Constants;
import com.jesseoberstein.alert.validation.Validatable;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.With;

import static java.util.Objects.isNull;

@With
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(
    tableName = "user_alarms",
    indices = {
        @Index("route_id"),
        @Index("stop_id"),
        @Index("direction_id"),
        @Index("repeat_type_id")},
    foreignKeys = {
        @ForeignKey(entity = Route.class, parentColumns = "id", childColumns = "route_id"),
        @ForeignKey(entity = Direction.class, parentColumns = "id", childColumns = "direction_id"),
        @ForeignKey(entity = Stop.class, parentColumns = {"id", "route_id", "direction_id"}, childColumns = {"stop_id", "route_id", "direction_id"})
    }
)
public class UserAlarm implements Serializable, Validatable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "route_id")
    private String routeId;

    @ColumnInfo(name = "direction_id")
    private Long directionId;

    @ColumnInfo(name = "stop_id")
    private String stopId;

    @ColumnInfo(name = "repeat_type_id")
    private long repeatTypeId;

    @Embedded
    private SelectedDays selectedDays;

    private Integer hour;
    private Integer minutes;
    private String nickname;
    private long duration;
    private boolean active;

    @Ignore
    private String time;

    @Ignore
    private RepeatType repeatType;

    @Ignore
    private String nextFiringDayString;

    @Ignore
    @Builder.Default
    private List<String> errors = new ArrayList<>();

    public UserAlarm() {
        this.repeatType = RepeatType.NEVER;
        this.selectedDays = new SelectedDays();
        this.duration = 30;
        this.active = true;
        this.errors = new ArrayList<>();

        LocalTime defaultTime = LocalTime.now().plusHours(1);
        this.hour = defaultTime.getHour();
        this.minutes = defaultTime.getMinute();
    }

    public void setRepeatType(RepeatType repeatType) {
        if (repeatType == this.repeatType) {
            return;
        }

        this.repeatType = repeatType;
        setRepeatTypeId(repeatType.getId());
        setSelectedDays(repeatType.getSelectedDays());
    }

    public UserAlarm withRoute(Route route) {
        return Optional.ofNullable(route)
                .map(Route::getId)
                .map(this::withRouteId)
                .orElse(this);
    }

    public UserAlarm withDirection(Direction direction) {
        return Optional.ofNullable(direction)
                .map(Direction::getId)
                .map(this::withDirectionId)
                .orElse(this);
    }

    public UserAlarm withStop(Stop stop) {
        return Optional.ofNullable(stop)
                .map(Stop::getId)
                .map(this::withStopId)
                .orElse(this);
    }

    @Override
    public boolean isValid() {
        this.errors.clear();

        if (RepeatType.CUSTOM.getId() == this.repeatTypeId && !this.selectedDays.isAnyDaySelected()) {
            this.errors.add(Constants.CUSTOM_REPEAT_TYPE);
        }
        if (isNull(this.routeId) || this.routeId.isEmpty()) {
            this.errors.add(Constants.ROUTE);
        }
        if (isNull(this.directionId) || this.directionId < 0) {
            this.errors.add(Constants.DIRECTION_ID);
        }
        if (isNull(this.stopId) || this.stopId.isEmpty()) {
            this.errors.add(Constants.STOP_ID);
        }

        return this.errors.isEmpty();
    }
}
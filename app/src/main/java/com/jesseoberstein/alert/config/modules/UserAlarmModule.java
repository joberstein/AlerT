package com.jesseoberstein.alert.config.modules;

import android.content.Context;

import com.jesseoberstein.alert.interfaces.AlarmDaySetter;
import com.jesseoberstein.alert.interfaces.AlarmDirectionSetter;
import com.jesseoberstein.alert.interfaces.AlarmDurationSetter;
import com.jesseoberstein.alert.interfaces.AlarmEndpointSetter;
import com.jesseoberstein.alert.interfaces.AlarmModifier;
import com.jesseoberstein.alert.interfaces.AlarmRepeatSetter;
import com.jesseoberstein.alert.interfaces.AlarmRouteSetter;
import com.jesseoberstein.alert.interfaces.AlarmStopSetter;
import com.jesseoberstein.alert.interfaces.AlarmTimeSetter;
import com.jesseoberstein.alert.utils.DateTimeHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import java.util.Calendar;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public class UserAlarmModule {

    @Reusable
    @Provides
    UserAlarmScheduler userAlarmScheduler(DateTimeHelper dateTimeHelper, Calendar calendar) {
        return new UserAlarmScheduler(dateTimeHelper, calendar);
    }

    @Provides
    AlarmModifier alarmModifier(Context context) {
        return (AlarmModifier) context;
    }

    @Provides
    AlarmDaySetter alarmDaySetter(Context context) {
        return (AlarmDaySetter) context;
    }

    @Provides
    AlarmDirectionSetter alarmDirectionSetter(Context context) {
        return (AlarmDirectionSetter) context;
    }

    @Provides
    AlarmDurationSetter alarmDurationSetter(Context context) {
        return (AlarmDurationSetter) context;
    }

    @Provides
    AlarmEndpointSetter alarmEndpointSetter(Context context) {
        return (AlarmEndpointSetter) context;
    }

    @Provides
    AlarmRepeatSetter alarmRepeatSetter(Context context) {
        return (AlarmRepeatSetter) context;
    }

    @Provides
    AlarmRouteSetter alarmRouteSetter(Context context) {
        return (AlarmRouteSetter) context;
    }

    @Provides
    AlarmStopSetter alarmStopSetter(Context context) {
        return (AlarmStopSetter) context;
    }

    @Provides
    AlarmTimeSetter alarmTimeSetter(Context context) {
        return (AlarmTimeSetter) context;
    }
}

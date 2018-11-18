package com.jesseoberstein.alert.utils;

import android.net.Uri;

import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

public class AlarmUtils {

    static Uri buildAlarmUri(long alarmId) {
        return new Uri.Builder().scheme("content").path("alarms/" + alarmId).build();
    }

    /**
     * Map an alarm's id and endpoints to a list of {@link AlarmEndpoint}.
     * @param alarmWithRelations containing an id and endpoints
     * @return a list of alarm endpoints
     */
    public static AlarmEndpoint[] createAlarmEndpoints(UserAlarmWithRelations alarmWithRelations) {
        return alarmWithRelations.getEndpoints().stream()
                .map(endpoint -> new AlarmEndpoint(alarmWithRelations.getAlarm().getId(), endpoint.getId()))
                .toArray(AlarmEndpoint[]::new);
    }
}

package com.jesseoberstein.alert.receivers;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.v2.Client;
import com.jesseoberstein.alert.models.v2.Connection;
import com.jesseoberstein.alert.models.v2.StreamSubscribeRequest;
import com.jesseoberstein.alert.utils.IntentBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;

public class OnAlarmStart extends BaseReceiver {

    @Inject
    protected IntentBuilder intentBuilder;

    @Inject
    protected AlarmManager alarmManager;

    @Inject
    protected AppDatabase db;

    @Inject
    protected RequestQueue requestQueue;

    @Override
    public void onReceive(Context context, Intent alarmStartIntent) {
        super.onReceive(context, alarmStartIntent);

        Optional.ofNullable(alarmStartIntent)
                .map(i -> i.getLongExtra(ALARM_ID, -1))
                .ifPresent(id -> db.userAlarmDao()
                        .getWithRelationsSingle(id)
                        .subscribeOn(Schedulers.computation())
                        .doOnError(e -> Log.e("OnAlarmStart.onReceive", e.getMessage()))
                        .subscribe(alarm -> subscribeToPredictions(context, alarm)));
    }

    public void subscribeToPredictions(Context context, UserAlarmWithRelations alarm) {
        Connection connection = Connection.builder()
                .routeId(alarm.getRoute().getId())
                .stopId(alarm.getStop().getId())
                .build();

        Client client = Client.builder()
                .directionId(Long.valueOf(alarm.getDirection().getDirectionId()).intValue())
                .ttl(Duration.ofMinutes(alarm.getAlarm().getDuration()))
                .build();

        StreamSubscribeRequest request = StreamSubscribeRequest.builder()
                .connection(connection)
                .client(client)
                .build();

        context.getSharedPreferences("mbta-alerts", Context.MODE_PRIVATE).edit().clear().apply();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);

        Tasks.whenAllSuccess(
                FirebaseInstallations.getInstance().getId(),
                FirebaseMessaging.getInstance().getToken()
        ).addOnSuccessListener(tasks -> {
            String fid = (String) tasks.get(0);
            String token = (String) tasks.get(1);

            request.setClient(request.getClient().toBuilder()
                    .id(fid)
                    .token(token)
                    .build());

            requestQueue.add(buildStreamSubscribeRequest(request, mapper));
        });
    }

    private JsonObjectRequest buildStreamSubscribeRequest(StreamSubscribeRequest request, ObjectMapper mapper) {
        JSONObject body = new JSONObject();

        try {
            body = new JSONObject(mapper.writeValueAsString(request));
        } catch (JsonProcessingException | JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(
                Request.Method.POST,
                "https://fast-basin-50825.herokuapp.com/streams",
                body,
                response -> Log.i("SUCCESS", "Subscribed to stream " + request.getConnection().toString()),
                error -> Log.e("FAILURE", "Failed to subscribe to stream " + request.getConnection().toString() + " with error: " + error)
        );
    }
}
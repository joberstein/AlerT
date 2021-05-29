package com.jesseoberstein.alert.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.installations.FirebaseInstallations;
import com.jesseoberstein.alert.models.v2.StreamUnsubscribeRequest;

import static com.jesseoberstein.alert.utils.Constants.MESSAGING_SERVICE_BASE_URL;

public class AlertDismissedReceiver extends BroadcastReceiver {

    private RequestQueue queue;

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", -1);

        if (notificationId > 0) {
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.cancel(notificationId);

            queue = Volley.newRequestQueue(context);

            FirebaseInstallations.getInstance().getId().addOnSuccessListener(fid -> {
                StreamUnsubscribeRequest unsubscribeRequest = StreamUnsubscribeRequest.builder()
                        .routeId(intent.getStringExtra("routeId"))
                        .stopId(intent.getStringExtra("stopId"))
                        .clientId(fid)
                        .build();

                StringRequest request = buildUnsubscribeStreamRequest(unsubscribeRequest);
                queue.add(request);
            });
        }
    }

    private StringRequest buildUnsubscribeStreamRequest(StreamUnsubscribeRequest request) {
        String streamId = String.join(":", request.getRouteId(), request.getStopId());
        String urlTemplate = MESSAGING_SERVICE_BASE_URL + "/streams/%s/clients/%s";

        return new StringRequest(
                Request.Method.DELETE,
                String.format(urlTemplate, streamId, request.getClientId()),
                response -> Log.i("SUCCESS", "Unsubscribed from stream " + streamId),
                error -> Log.e("FAILURE", "Failed to unsubscribe from stream " + streamId)
        );
    }
}

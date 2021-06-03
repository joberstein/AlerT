package com.jesseoberstein.alert.network;

import android.net.Uri;

//import com.jesseoberstein.alert.sensitive.SensitiveData;

public class UrlBuilder {

    public String buildPredictionRequestUrl(String routeId, String stopId, int directionId) {
        return new Uri.Builder()
                .scheme("https")
                .authority("api-v3.mbta.com")
                .appendPath("predictions")
                .appendQueryParameter("filter[route]", routeId)
                .appendQueryParameter("filter[stop]", stopId)
                .appendQueryParameter("filter[direction_id]", String.valueOf(directionId))
                .appendQueryParameter("include", "trip")
//                .appendQueryParameter("api_key", SensitiveData.MBTA_API_KEY)
                .build()
                .toString();
    }
}

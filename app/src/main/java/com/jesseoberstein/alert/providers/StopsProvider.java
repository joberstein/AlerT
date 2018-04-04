package com.jesseoberstein.alert.providers;

import android.content.res.AssetManager;

import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.utils.ResponseParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.jesseoberstein.alert.utils.Constants.MBTA_DATA_DATE;

public class StopsProvider {
    private static StopsProvider instance = null;
    private static Map deserializedStopsByRoute;

    private StopsProvider(AssetManager assetManager) {
        deserializedStopsByRoute = serializeStopsMap(assetManager);
    }

    public static StopsProvider init(AssetManager assetManager) {
        if (null == instance) {
            instance = new StopsProvider(assetManager);
        }
        return instance;
    }

    public List<Stop> getStopsByRoute(String routeId) {
        byte[] serializedBytes = ResponseParser.readBytes((String) deserializedStopsByRoute.get(routeId));
        List<Stop> stops = ResponseParser.parseJSONApi(serializedBytes, Stop.class);
        return stops;
    }

    private Map serializeStopsMap(AssetManager assetManager) {
        try {
            InputStream inputStream = assetManager.open( MBTA_DATA_DATE +"/stops.json");
            return ResponseParser.parseFromFile(inputStream, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }
}

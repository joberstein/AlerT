package com.jesseoberstein.mbta.api;

import com.jesseoberstein.mbta.utils.ResponseParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import static com.jesseoberstein.mbta.utils.UrlBuilder.urlBuilder;

class Fetch {

    static <T> Collection<T> fetch(String endpoint, String query, Class<T> responseClass) {
        URL url;
        HttpURLConnection urlConnection = null;
        Collection<T> response = Collections.emptyList();
        url = urlBuilder().withEndpoint(endpoint).withQuery(query).build();
        if (url == null) {
            return response;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            response = ResponseParser.parseJSONApi(urlConnection.getInputStream(), responseClass);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }

        return response;
    }
}

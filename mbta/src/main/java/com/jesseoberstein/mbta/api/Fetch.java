package com.jesseoberstein.mbta.api;

import com.jesseoberstein.mbta.utils.ResponseParser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static com.jesseoberstein.mbta.utils.UrlBuilder.urlBuilder;

class Fetch extends BaseRequest {

    static <T> Optional<T> fetch(String endpoint, String query, Class<T> responseClass) {
        URL url;
        HttpURLConnection urlConnection = null;
        Optional<T> response = Optional.empty();

        try {
            url = urlBuilder().withEndpoint(endpoint).withQuery(query).build();
        } catch (MalformedURLException e) {
            System.out.println(e);
            return response;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            String content = ResponseParser.read(new BufferedInputStream(urlConnection.getInputStream()));
            response = ResponseParser.parse(content, responseClass);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }

        return response;
    }
}

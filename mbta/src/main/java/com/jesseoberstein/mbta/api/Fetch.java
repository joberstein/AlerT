package com.jesseoberstein.mbta.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jesseoberstein.mbta.utils.UrlBuilder.*;

class Fetch extends BaseRequest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String basePath = "app/src/main/assets/";

    static <T> Optional<T> fetch(String endpoint, String query, Class<T> responseClass) {
        URL url;
        HttpURLConnection urlConnection = null;
        T response = null;

        try {
            url = urlBuilder().withEndpoint(endpoint).withQuery(query).build();
        } catch (MalformedURLException e) {
            System.out.println(e);
            return Optional.empty();
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            String content = read(new BufferedInputStream(urlConnection.getInputStream()));
            Files.write(Paths.get(basePath + endpoint + "_response.json"), content.getBytes());
            response = mapper.readValue(content, responseClass);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }

        return Optional.ofNullable(response);
    }

    private static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}

package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.network.UrlBuilder;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;

public class UrlBuilderTest {
    private UrlBuilder urlBuilder;

    @Before
    public void setUrlBuilder() {
        urlBuilder = new UrlBuilder();
    }

    @Test
    public void testBuildNotificationRequestUrl() throws UnsupportedEncodingException {
        String apiKey = System.getenv("MBTA_API_KEY");
        String url = urlBuilder.buildPredictionRequestUrl("routeId", "stopId", 1);
        assertThat(url, Matchers.startsWith("https://api-v3.mbta.com/predictions?"));
        assertThat(url, Matchers.containsString(getEncodedString("filter[route]") + "=routeId"));
        assertThat(url, Matchers.containsString(getEncodedString("filter[stop]") + "=stopId"));
        assertThat(url, Matchers.containsString(getEncodedString("filter[direction_id]") + "=1"));
        assertThat(url, Matchers.containsString("include=trip"));
//        assertThat(url, Matchers.containsString("api_key=" + SensitiveData.MBTA_API_KEY));
    }

    private String getEncodedString(String input) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, StandardCharsets.UTF_8.name());
    }
}

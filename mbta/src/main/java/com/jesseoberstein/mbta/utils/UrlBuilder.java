package com.jesseoberstein.mbta.utils;

import java.net.MalformedURLException;
import java.net.URL;

//import static com.jesseoberstein.mbta.sensitive.SensitiveData.API_KEY;

public class UrlBuilder {
    private static final String PROTOCOL = "https";
    private static final String HOST = "api-v3.mbta.com";

    private int port;
    private String endpoint;
    private QueryBuilder query;

    private UrlBuilder() {
        this.port = -1;
        this.endpoint = "";
//        this.query = QueryBuilder.queryBuilder().addParam("api_key", API_KEY);
    }

    public static UrlBuilder urlBuilder() {
        return new UrlBuilder();
    }

    public UrlBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public UrlBuilder withEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public UrlBuilder withQuery(String query) {
        this.query = this.query.addParams(query);
        return this;
    }

    public URL build() {
        String hostName = (this.port < 0) ? HOST : HOST + ":" + this.port;
        try {
            return new URL(String.format("%s://%s/%s/?%s", PROTOCOL, hostName, this.endpoint, this.query.build()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

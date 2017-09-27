package com.jesseoberstein.mbta.utils;

import java.net.MalformedURLException;
import java.net.URL;

import static com.jesseoberstein.mbta.api.BaseRequest.BASE_HOST;
import static com.jesseoberstein.mbta.api.BaseRequest.BASE_PATH;
import static com.jesseoberstein.mbta.api.BaseRequest.BASE_PROTOCOL;
import static com.jesseoberstein.mbta.sensitive.SensitiveData.*;

public class UrlBuilder {
    private String protocol;
    private String host;
    private int port;
    private String basePath;
    private String endpoint;
    private QueryBuilder query;

    private UrlBuilder() {
        this.protocol = BASE_PROTOCOL;
        this.host = BASE_HOST;
        this.port = -1;
        this.basePath = BASE_PATH;
        this.endpoint = "";
        this.query = QueryBuilder.queryBuilder().addParam("api_key", API_KEY);
    }

    public static UrlBuilder urlBuilder() {
        return new UrlBuilder();
    }

    public UrlBuilder withProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public UrlBuilder withHost(String host) {
        this.host = host;
        return this;
    }

    public UrlBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public UrlBuilder withBasePath(String basePath) {
        this.basePath = basePath;
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
        String hostName = (this.port < 0) ? this.host : this.host + ":" + this.port;
        try {
            return new URL(String.format("%s://%s/%s/%s/?%s", this.protocol, hostName, this.basePath, this.endpoint, this.query.build()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.jesseoberstein.mbta.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class QueryBuilder {
    private Map<String, String> params;

    private QueryBuilder() {
        this.params = new HashMap<>();
    }

    static QueryBuilder queryBuilder() {
        return new QueryBuilder();
    }

    QueryBuilder addParam(String key, String value) {
        String normalKey = normalize(key);
        if (!this.params.containsKey(normalKey)) {
            this.params.put(normalKey, normalize(value));
        }
        return this;
    }

    private QueryBuilder addParam(String param) {
        String[] paramParts = param.split("=");
        if (paramParts.length > 1) {
            this.addParam(paramParts[0], paramParts[1]);
        }
        return this;
    }

    QueryBuilder addParams(String params) {
        String[] paramList = params.split("&");
        Arrays.stream(paramList).forEach(this::addParam);
        return this;
    }

    private String normalize(String val) {
        return val.trim().toLowerCase().replace(" ", "_");
    }

    String build() {
        return this.params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }
}

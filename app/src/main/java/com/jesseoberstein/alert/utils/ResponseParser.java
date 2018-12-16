package com.jesseoberstein.alert.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.jesseoberstein.alert.models.mbta.Prediction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.models.mbta.Trip;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.github.jasminb.jsonapi.SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES;

/**
 * A utility class for parsing content to a Java object.
 */
public class ResponseParser {
    private static ObjectMapper mapper;
    private static ResourceConverter converter;

    private static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper()
                    .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
                    .enable(INDENT_OUTPUT)
                    .disable(ADJUST_DATES_TO_CONTEXT_TIME_ZONE);  // fixes coercing zone to UTC
        }

        return mapper;
    }

    private static ResourceConverter getResourceConverter() {
        if (converter == null) {
            converter = new ResourceConverter(getObjectMapper(),
                    Route.class, Stop.class, Trip.class, Prediction.class);
            converter.enableSerializationOption(INCLUDE_RELATIONSHIP_ATTRIBUTES);
        }

        return converter;
    }

    /**
     * Parse the given input stream into a list of objects of the given class.
     * @param content The content to parseJSONApi.
     * @param objectClass The class to parseJSONApi the content into.
     * @param <T> The type of the object class to parse.
     * @return The list of parsed objects, or an empty list if there was an error parsing.
     */
    public static <T> List<T> parseJSONApi(InputStream content, Class<T> objectClass) {
        try {
            return getResourceConverter().readDocumentCollection(content, objectClass).get();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

package com.jesseoberstein.alert.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.jesseoberstein.alert.models.mbta.Prediction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.models.mbta.Trip;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A utility class for parsing content to a Java object.
 */
public class ResponseParser {
    private static ObjectMapper mapper;
    private static ResourceConverter converter;

    private static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);  // fixes coercing zone to UTC
        }

        return mapper;
    }

    private static ResourceConverter getResourceConverter() {
        if (converter == null) {
            converter = new ResourceConverter(getObjectMapper(),
                    Route.class, Stop.class, Trip.class, Prediction.class);
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

    /**
     * Format the given date time.
     * @param zonedDateTime The date time to format.
     * @return The short version string of the date time.
     */
    public static String formatZonedTime(Date zonedDateTime) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ENGLISH);
        return (zonedDateTime != null) ? dateFormat.format(zonedDateTime) : "";
    }
}

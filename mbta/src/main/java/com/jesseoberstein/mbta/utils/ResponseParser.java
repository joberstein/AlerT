package com.jesseoberstein.mbta.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import com.jesseoberstein.mbta.model.Prediction;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.model.Trip;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A utility class for parsing content to a Java object.
 */
public class ResponseParser {
    private static final String TODAY = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    private static final String BASE_WRITE_PATH = "../app/src/main/assets/" + TODAY + "/";
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
     * @param <T> The type of the object class to parseJSONApi.
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
     * Parse the given input stream into a list of objects of the given class.
     * @param content The content to parse.
     * @param <T> The type of the object class to parse.
     * @return The list of parsed objects, or an empty list if there was an error parsing.
     */
    public static <T> List<T> parseJSONArray(InputStream content, Class<T> objectClass) {
        try {
            JavaType objectListType = getObjectMapper().getTypeFactory().constructCollectionType(List.class, objectClass);
            return getObjectMapper().readValue(content, objectListType);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static String formatZonedTime(Date zonedDateTime) {
        return zonedDateTime == null ?
                null :
                DateFormat.getDateInstance(DateFormat.SHORT, Locale.ENGLISH).format(zonedDateTime);
    }

    /**
     * Write an object to a file with the given name prefix.
     * @param object The object to write to a file.
     * @param fileName The prefix of the file name ('_response.json' will be appended).
     *                 Does not accept a path, only a filename prefix.
     * @param <T> The type of object to write to a file.
     */
    public static <T> void write(T object, String fileName) {
        try {
            String content = getObjectMapper().writeValueAsString(object);
            Path writePath = Paths.get(BASE_WRITE_PATH + fileName + ".json").toAbsolutePath();
            Files.createDirectories(Paths.get(BASE_WRITE_PATH));
            Files.write(writePath, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void writeJSONApi(List<T> objects, String fileName) throws DocumentSerializationException {
        try {
            byte[] content = getResourceConverter().writeDocumentCollection(new JSONAPIDocument<>(objects));
            Path writePath = Paths.get(BASE_WRITE_PATH + fileName + ".json").toAbsolutePath();
            Files.createDirectories(Paths.get(BASE_WRITE_PATH));
            Files.write(writePath, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

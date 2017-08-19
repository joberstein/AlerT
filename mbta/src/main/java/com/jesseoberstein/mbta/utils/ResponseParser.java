package com.jesseoberstein.mbta.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A utility class for parsing content to a Java object.
 */
public class ResponseParser {
    private final static ObjectMapper mapper = new ObjectMapper();
    private static final String basePath = "app/src/main/assets/";

    /**
     * Parse the given content string into an object of the given class.
     * @param content The content to parse.
     * @param objectClass The class to parse the content into.
     * @param <T> The type of the object class to parse.
     * @return The optional result of the given class type that was parsed.
     */
    public static <T> Optional<T> parse(String content, Class<T> objectClass) {
        try {
            return Optional.of(mapper.readValue(content, objectClass));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Read input from an input stream and combine it all as a string.
     * @param input An InputStream of content, probably an HTTP response body.
     * @return The read content as a String.
     * @throws IOException when there's an error reading the input stream.
     */
    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Write an object to a file with the given name prefix.
     * @param object The object to write to a file.
     * @param fileName The prefix of the file name ('_response.json' will be appended).
     *                 Does not accept a path, only a filename prefix.
     * @param <T> The type of object to write to a file.
     */
    public static <T> void write(T object, String fileName) {
        String content = null;
        try {
            content = mapper.writeValueAsString(object);
            Files.write(Paths.get(basePath + fileName + "_response.json"), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.jesseoberstein.alert.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class FileHelper {
    private final Context context;

    @Inject
    public FileHelper(Context context) {
        this.context = context;
    }

    /**
     * Read in the the given asset file.
     * @param filename the name of the asset file, including extension.
     * @return A list of the lines in the file.
     */
    List<String> readFile(String filename) {
        try {
            InputStream inputStream = context.getAssets().open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            List<String> content = reader.lines().collect(Collectors.toList());
            reader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("File at '" + filename + "' not found.", e.getMessage());
        }

        return Collections.emptyList();
    }
}

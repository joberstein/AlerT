package com.jesseoberstein.alert.network;

import com.android.volley.Request;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpResponse;
import com.jesseoberstein.alert.utils.FileHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Fake {@link BaseHttpStack} that returns the fake content using a resource file.
 */
public class FakeHttpStack extends BaseHttpStack {
    private final FileHelper fileHelper;

    public FakeHttpStack(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) {
        String filename = request.getUrl();

        try {
            String content = fileHelper.readFile(filename).stream().collect(Collectors.joining("\n"));
            InputStream contentBytes = new ByteArrayInputStream(content.getBytes());
            return new HttpResponse(200, Collections.emptyList(), content.length(), contentBytes);
        } catch (IOException e) {
            return new HttpResponse(404, Collections.emptyList());
        }
    }
}
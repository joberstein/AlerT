package com.jesseoberstein.alert.utils;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FileHelperTest {

    private FileHelper fileHelper;

    @Before
    public void setup() {
        fileHelper = new FileHelper(InstrumentationRegistry.getContext());
    }

    @Test
    public void testReadFile() throws IOException {
        List<String> fileLines = fileHelper.readFile("fileHelperTest.txt");
        assertThat(Arrays.asList("Test", "File Helper"), equalTo(fileLines));
    }

    @Test(expected = IOException.class)
    public void testFileNotFound() throws IOException {
        assertEquals(Collections.emptyList(), fileHelper.readFile("fake-file.txt"));
    }
}
package com.jesseoberstein.alert.utils;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.Assert.assertEquals;

public class FileHelperTest {

    private FileHelper fileHelper;

    @Before
    public void setup() {
        fileHelper = new FileHelper(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testReadFile() {
        assertEquals(Collections.emptyList(), fileHelper.readFile("fileHelperTest.txt"));
    }

    @Test
    public void testFileNotFound() {
        assertEquals(Collections.emptyList(), fileHelper.readFile("fake-file.txt"));
    }
}
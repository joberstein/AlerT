package com.jesseoberstein.alert.data.database;

import android.content.Context;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jesseoberstein.alert.utils.DatabaseCallbackBuilder;
import com.jesseoberstein.alert.utils.FileHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private AppDatabase testDb;

    @Before
    public void createDb() throws IOException {
        Context context = InstrumentationRegistry.getContext();
        FileHelper fileHelper = new FileHelper(context);
        DatabaseCallbackBuilder databaseCallbackBuilder = new DatabaseCallbackBuilder(fileHelper);
        testDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .addCallback(databaseCallbackBuilder.buildCreateDbCallback())
                .build();
    }

    @Test
    public void testRoutesInserted() {
        assertEquals( 11, testDb.routeDao().count());
    }

    @Test
    public void testStopsInserted() {
        assertEquals(9, testDb.stopDao().count());
    }

    @Test
    public void testDirectionsInserted() {
        assertEquals(8, testDb.directionDao().count());
    }

    @Test
    public void testEndpointsInserted() {
        assertEquals(7, testDb.endpointDao().count());
    }
}

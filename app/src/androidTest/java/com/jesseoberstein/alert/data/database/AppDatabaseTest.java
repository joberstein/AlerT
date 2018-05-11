package com.jesseoberstein.alert.data.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
        testDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .addCallback(AppDatabase.buildCallback(context))
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

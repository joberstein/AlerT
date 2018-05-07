package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.jesseoberstein.alert.data.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;

public abstract class BaseDaoTest<T> {
    private AppDatabase testDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        testDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }

    @After
    public void closeDb() throws IOException {
        testDb.close();
    }

    abstract BaseDao<T> getDao();
    abstract T[] getTestElements();

    @Test
    public void testBaseMethods() throws Exception {
        verifyDatabaseIsEmpty();
        getDao().insert(getTestElements());
        assertEquals(getTestElements().length, getDao().count());
        assertEquals(Arrays.asList(getTestElements()), getDao().getAll());
        verifyDatabaseIsEmpty();
    }

    AppDatabase getTestDatabase() {
        return testDb;
    }

    private void verifyDatabaseIsEmpty() {
        getDao().delete(getTestElements());
        assertEquals(0, getDao().count());
        assertEquals(Collections.emptyList(), getDao().getAll());
    }
}

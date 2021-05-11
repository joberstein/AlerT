package com.jesseoberstein.alert.data.dao;

import android.content.Context;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;

import com.jesseoberstein.alert.data.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;

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

    AppDatabase getTestDatabase() {
        return testDb;
    }

    @Test
    public void testBaseMethods() throws Exception {
        verifyDatabaseIsEmpty();
        getDao().insert(getTestElements());
        assertEquals(getTestElements().length, getDao().count());
        assertEquals(Arrays.asList(getTestElements()), getDao().getAll());
        verifyDatabaseIsEmpty();
    }

    void assertDaoResults(List<T> results, List<Integer> expectedIndices) {
        assertThat(results, hasSize(expectedIndices.size()));
        expectedIndices.forEach(idx -> assertThat(results, hasItem(getTestElements()[idx])));
    }

    private void verifyDatabaseIsEmpty() {
        getDao().delete(getTestElements());
        assertEquals(0, getDao().count());
        assertEquals(Collections.emptyList(), getDao().getAll());
    }
}

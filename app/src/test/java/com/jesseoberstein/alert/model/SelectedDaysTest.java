package com.jesseoberstein.alert.model;

import com.jesseoberstein.alert.models.SelectedDays;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SelectedDaysTest {
    private SelectedDays testSelectedDays;

    @Before
    public void setup() {
        testSelectedDays = new SelectedDays();
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionIfWeekdayArrayIsWrongLength() {
        new SelectedDays(new int[]{1});
    }

    @Test
    public void testNoArgumentConstructor() {
        int[] expected = new int[]{0, 0, 0, 0, 0, 0, 0};
        assertTrue(Arrays.equals(expected, testSelectedDays.toIntArray()));
    }

    @Test
    public void testConstructorWithDayArray() {
        int[] expected = new int[]{0, 1, 1, 0, 1, 1, 1};
        assertTrue(Arrays.equals(expected, new SelectedDays(expected).toIntArray()));
    }

    @Test
    public void testConstructorWithDays() {
        int[] expected = new int[]{1, 1, 1, 0, 1, 1, 0};
        SelectedDays testSelectedDays = new SelectedDays(1, 1, 1, 0, 1, 1, 0);
        assertTrue(Arrays.equals(expected, testSelectedDays.toIntArray()));
    }

    @Test
    public void testSunday() {
        assertEquals(0, testSelectedDays.getSunday());
        testSelectedDays.setSunday(1);
        assertEquals(1, testSelectedDays.getSunday());
    }

    @Test
    public void testMonday() {
        assertEquals(0, testSelectedDays.getMonday());
        testSelectedDays.setMonday(1);
        assertEquals(1, testSelectedDays.getMonday());
    }

    @Test
    public void testTuesday() {
        assertEquals(0, testSelectedDays.getTuesday());
        testSelectedDays.setTuesday(1);
        assertEquals(1, testSelectedDays.getTuesday());
    }

    @Test
    public void testWednesday() {
        assertEquals(0, testSelectedDays.getWednesday());
        testSelectedDays.setWednesday(1);
        assertEquals(1, testSelectedDays.getWednesday());
    }

    @Test
    public void testThursday() {
        assertEquals(0, testSelectedDays.getThursday());
        testSelectedDays.setThursday(1);
        assertEquals(1, testSelectedDays.getThursday());
    }

    @Test
    public void testFriday() {
        assertEquals(0, testSelectedDays.getFriday());
        testSelectedDays.setFriday(1);
        assertEquals(1, testSelectedDays.getFriday());
    }

    @Test
    public void testSaturday() {
        assertEquals(0, testSelectedDays.getSaturday());
        testSelectedDays.setSaturday(1);
        assertEquals(1, testSelectedDays.getSaturday());
    }

    @Test(expected = RuntimeException.class)
    public void testSetDay_invalidDay() {
        testSelectedDays.setDay(9, true);
    }

    @Test
    public void testSetDay() {
        assertEquals(0, testSelectedDays.getSunday());
        testSelectedDays.setDay(0, true);
        assertEquals(1, testSelectedDays.getSunday());
        testSelectedDays.setDay(0, false);
        assertEquals(0, testSelectedDays.getSunday());
    }

    @Test
    public void testToIntArray() {
        testSelectedDays.setMonday(1);
        testSelectedDays.setWednesday(1);
        testSelectedDays.setSaturday(1);
        int[] expected = new int[]{0, 1, 0, 1, 0, 0, 1};
        assertTrue(Arrays.equals(expected, testSelectedDays.toIntArray()));
    }

    @Test
    public void testToBooleanArray() {
        testSelectedDays.setMonday(1);
        testSelectedDays.setWednesday(1);
        testSelectedDays.setSaturday(1);
        boolean[] expected = new boolean[]{false, true, false, true, false, false, true};
        assertTrue(Arrays.equals(expected, testSelectedDays.toBooleanArray()));
    }
}

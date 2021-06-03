package com.jesseoberstein.alert.model;

import com.jesseoberstein.alert.models.SelectedDays;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SelectedDaysTest {
    private SelectedDays selectedDays;

    @Before
    public void setup() {
        selectedDays = new SelectedDays();
    }

    @Test
    public void testSunday() {
        assertEquals(0, selectedDays.getSunday());
        selectedDays.setSunday(1);
        assertEquals(1, selectedDays.getSunday());
    }

    @Test
    public void testMonday() {
        assertEquals(0, selectedDays.getMonday());
        selectedDays.setMonday(1);
        assertEquals(1, selectedDays.getMonday());
    }

    @Test
    public void testTuesday() {
        assertEquals(0, selectedDays.getTuesday());
        selectedDays.setTuesday(1);
        assertEquals(1, selectedDays.getTuesday());
    }

    @Test
    public void testWednesday() {
        assertEquals(0, selectedDays.getWednesday());
        selectedDays.setWednesday(1);
        assertEquals(1, selectedDays.getWednesday());
    }

    @Test
    public void testThursday() {
        assertEquals(0, selectedDays.getThursday());
        selectedDays.setThursday(1);
        assertEquals(1, selectedDays.getThursday());
    }

    @Test
    public void testFriday() {
        assertEquals(0, selectedDays.getFriday());
        selectedDays.setFriday(1);
        assertEquals(1, selectedDays.getFriday());
    }

    @Test
    public void testSaturday() {
        assertEquals(0, selectedDays.getSaturday());
        selectedDays.setSaturday(1);
        assertEquals(1, selectedDays.getSaturday());
    }

    @Test(expected = RuntimeException.class)
    public void testSetDay_invalidDay() {
        selectedDays = selectedDays.update(9, true);
    }

    @Test
    public void testSetDay() {
        assertEquals(0, selectedDays.getSunday());
        selectedDays = selectedDays.update(0, true);
        assertEquals(1, selectedDays.getSunday());
        selectedDays = selectedDays.update(0, false);
        assertEquals(0, selectedDays.getSunday());
    }

    @Test
    public void testToIntArray() {
        selectedDays.setMonday(1);
        selectedDays.setWednesday(1);
        selectedDays.setSaturday(1);
        int[] expected = new int[]{0, 1, 0, 1, 0, 0, 1};
        assertTrue(Arrays.equals(expected, selectedDays.toIntArray()));
    }

    @Test
    public void testToBooleanArray() {
        selectedDays.setMonday(1);
        selectedDays.setWednesday(1);
        selectedDays.setSaturday(1);
        boolean[] expected = new boolean[]{false, true, false, true, false, false, true};
        assertTrue(Arrays.equals(expected, selectedDays.toBooleanArray()));
    }

    @Test
    public void testIsAnyDaySelected() {
        // No days selected initially.
        assertFalse(selectedDays.isAnyDaySelected());

        // Re-check after setting a day.
        selectedDays.setFriday(1);
        assertTrue(selectedDays.isAnyDaySelected());
    }
}

package com.jesseoberstein.alert.test;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.TimePicker;

import org.hamcrest.Matcher;

import static junit.framework.Assert.assertEquals;

public class TestUtils {
    public static ViewAction setTime(final int hour, final int minute) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                TimePicker timePicker = (TimePicker) view;
                timePicker.setHour(hour);
                timePicker.setMinute(minute);
            }

            @Override
            public String getDescription() {
                return "Set the passed time into the TimePicker";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TimePicker.class);
            }
        };
    }

    public static ViewAssertion withTime(int hour, int minute) {
        return (view, noViewFoundException) -> {
            TimePicker timePicker = (TimePicker) view;
            assertEquals(hour, timePicker.getHour());
            assertEquals(minute, timePicker.getMinute());
        };
    }
}

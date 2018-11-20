package com.jesseoberstein.alert.test;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import org.hamcrest.Description;
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

    public static Matcher<View> hasDrawableColor(final int colorReference) {
        int color = InstrumentationRegistry.getTargetContext().getResources().getColor(colorReference, null);

        return new BoundedMatcher<View, ImageView>(ImageView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("drawable color: ").appendValue(color);
            }

            @Override
            protected boolean matchesSafely(ImageView foundView) {
                ColorFilter actual = foundView.getColorFilter();
                ColorFilter expected = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
                return expected.equals(actual);
            }
        };
    }
}

package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.test.TestUtils;
import com.jesseoberstein.alert.utils.DateTimeUtils;

import org.junit.Test;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AlarmTimeTest extends BaseEditAlarmTest {

    @Test
    public void timeSectionLabelAndValue() {
        moveToTimeSettingsTab();

        // Check time label text.
        onView(withId(R.id.alarmSettings_section_label_time))
                .check(matches(withText(R.string.time)));

        Calendar calendar = Calendar.getInstance();
        int defaultHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        int defaultMinute = calendar.get(Calendar.MINUTE);
        String defaultTime = DateTimeUtils.getFormattedTime(defaultHour, defaultMinute);

        // Check default time is set (one hour ahead of the current time).
        onView(withId(R.id.alarmSettings_section_value_time))
                .check(matches(withText(defaultTime)));

        // Click on time section.
        onView(withId(R.id.alarmSettings_time)).perform(click());

        // Set new alarm time.
        onView(withId(R.id.alarm_time_picker)).perform(TestUtils.setTime(14, 7));

        // Save new time.
        onView(withText("Ok")).perform(click());

        // Check new alarm time is saved.
        onView(withId(R.id.alarmSettings_section_value_time))
                .check(matches(withText("2:07 pm")));
    }
}

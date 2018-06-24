package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.RepeatType;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AlarmRepeatTest extends BaseEditAlarmSectionTest {
    private static final String DEFAULT_REPEAT = "Never";
    private static final String SELECTED_REPEAT = RepeatType.CUSTOM.toString();
    private static final String[] SELECTED_DAYS = {"Monday", "Wednesday", "Saturday"};

    @Before
    public void prepare() {
        moveToTimeSettingsTab();
        confirmRepeatLabelAndDefaultValue();
    }

    @Test
    public void repeatSectionLabelAndValue() throws InterruptedException {
        openRepeatDialog();
        selectRepeatFromList(SELECTED_REPEAT);
        selectDays(SELECTED_DAYS);

        confirmRepeatSelected(SELECTED_REPEAT);
        confirmDaysSelected(SELECTED_DAYS);
    }

    @Test
    public void invalidCustomRepeatShowsValidationError() throws InterruptedException {
        openRepeatDialog();
        selectRepeatFromList(SELECTED_REPEAT);
        selectDays();

        verifyErrorMessageShowsOnSave(R.string.repeat_custom_invalid);
        selectRepeatFromList(SELECTED_REPEAT);
        selectDays(SELECTED_DAYS);
        verifyErrorMessageNotShownOnSave(R.string.repeat_custom_invalid);
    }

    private void openRepeatDialog() {
        openSectionDialog(R.id.alarmSettings_repeat);
    }

    private void selectRepeatFromList(String repeat) throws InterruptedException {
        selectItem(repeat);
    }

    private void selectDays(String... days) throws InterruptedException {
        selectItems(days);
    }

    private void confirmRepeatSelected(String repeat) throws InterruptedException {
        // Check that the repeat string is correct.
        onView(withId(R.id.alarmSettings_section_value_repeat)).check(matches(withText(repeat)));
        openRepeatDialog();

        // Check that the repeat selection persists in the dialog.
        onView(withText(repeat)).check(matches(isChecked()));
        selectRepeatFromList(repeat);
    }

    private void confirmDaysSelected(String... days) {
        Arrays.stream(days).forEach(day -> onView(withText(day)).check(matches(isChecked())));
    }

    private void confirmRepeatLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultValue(
            R.id.alarmSettings_section_label_repeat, R.string.repeat,
            R.id.alarmSettings_section_value_repeat, DEFAULT_REPEAT
        );
    }
}

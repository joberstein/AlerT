package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AlarmNameTest extends BaseEditAlarmTest {
    private static final String TEST_NICKNAME = "Test Name";

    @Test
    public void nameSectionLabelAndValue() {
        moveToTimeSettingsTab();
        confirmNameLabelAndDefaultValue();
        openNicknameDialog();
        setNickname(TEST_NICKNAME);
        confirmNicknameSet(TEST_NICKNAME);
    }

    private void openNicknameDialog() {
        openSectionDialog(R.id.alarmSettings_name);
    }

    private void setNickname(String nickname) {
        onView(withId(R.id.alarm_nickname))
                .perform(replaceText(nickname))
                .perform(pressImeActionButton());
    }

    private void confirmNicknameSet(String nickname) {
        // Check that the nickname string is correct.
        onView(withId(R.id.alarmSettings_section_value_name)).check(matches(withText(nickname)));
        openNicknameDialog();

        // Check that the nickname selection persists in the dialog.
        onView(withId(R.id.alarm_nickname)).check(matches(withText(nickname)));
        setNickname(nickname);
    }

    private void confirmNameLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultHint(
            R.id.alarmSettings_section_label_name, R.string.nickname,
            R.id.alarmSettings_section_value_name, R.string.nickname_default
        );
    }
}

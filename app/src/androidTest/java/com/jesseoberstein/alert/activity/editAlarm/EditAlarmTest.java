package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmDirectionTest.selectDirection;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmRouteTest.selectRoute;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmStopTest.selectStop;
import static junit.framework.Assert.assertTrue;

public class EditAlarmTest extends BaseEditAlarmSectionTest {

    @Before
    public void prepare() {
        moveToMbtaSettingsTab();
    }

    @Test
    public void alarmIsNotSavedOnBack() {
        pressBackUnconditionally();
        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void alarmIsSaved() throws InterruptedException {
        selectRoute("Orange Line");
        relaunchActivity();
        selectDirection("Southbound");
        selectStop("Haymarket");
        saveAlarm();
        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void validationSnackbarHiddenAfterSectionClick() throws InterruptedException {
        selectRoute("Orange Line");
        relaunchActivity();
        selectDirection("Northbound");
        saveAlarm();
        verifyErrorMessageDisplayed(R.string.stop_invalid);
        selectDirection("Northbound");
        verifyErrorMessageNotDisplayed(R.string.stop_invalid);
    }
}

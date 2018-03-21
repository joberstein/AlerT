package com.jesseoberstein.alert.listeners.alarm;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TimePicker;

import com.jesseoberstein.alert.models.UserAlarm;

import static com.jesseoberstein.alert.utils.ActivityUtils.setSectionValueText;

/**
 * A listener that fires when the alarm time is set. Updates the appropriate section value text.
 */
public class OnSectionTimeSet extends OnSectionAction implements TimePickerDialog.OnTimeSetListener {

    public OnSectionTimeSet(View section, UserAlarm alarm) {
        super(section, alarm);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        UserAlarm alarm = getAlarm();
        alarm.setHour(hour);
        alarm.setMinutes(minute);
        setSectionValueText(getSection(), alarm.getTime(), 24);
    }
}

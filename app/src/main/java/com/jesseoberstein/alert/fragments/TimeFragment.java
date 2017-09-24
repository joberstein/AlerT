package com.jesseoberstein.alert.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Calendar;

import static java.util.Calendar.HOUR;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.getInstance;

public class TimeFragment extends AlarmBaseFragment implements OnAlarmSubmit {
    private TimePicker timePicker;
    private UserAlarm alarm;

    public static TimeFragment newInstance(int page) {
        return (TimeFragment) AlarmBaseFragment.newInstance(page, new TimeFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_time, container, false);
        alarm = ((EditAlarm) getActivity()).getAlarm();

        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_1);

        Calendar calendar = getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        boolean isUpdate = alarm.getId() > 0;

        timePicker = (TimePicker) view.findViewById(R.id.alarm_time_picker);
        timePicker.setHour(isUpdate ? alarm.getHour() : calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(isUpdate ? alarm.getMinutes() : calendar.get(MINUTE));

        return view;
    }

    /**
     * Send a bundle with the hour (0-23) and minutes (0-59) the time picker is set to.
     */
    @Override
    public void onAlarmSubmit() {
        alarm.setHour(timePicker.getHour());
        alarm.setMinutes(timePicker.getMinute());
    }
}

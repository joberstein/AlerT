package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.listeners.alarm.OnSectionTimeSet;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Optional;

import static android.view.View.OnClickListener;
import static com.jesseoberstein.alert.utils.ActivityUtils.setSectionLabelText;
import static com.jesseoberstein.alert.utils.ActivityUtils.setSectionValueText;

public class TimeSettingsFragment extends AlarmBaseFragment implements OnAlarmSubmit {
    private UserAlarm currentAlarm;
    private UserAlarm newAlarm;

    public static TimeSettingsFragment newInstance(int page) {
        return (TimeSettingsFragment) AlarmBaseFragment.newInstance(page, new TimeSettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_settings_time, container, false);

        // Clone the alarm, just in case the user decides to cancel their edits.
        // The alarms will be merged if the user submits their changes.
        currentAlarm = ((EditAlarm) getActivity()).getAlarm();
        newAlarm = currentAlarm.clone();

        View timeSection = view.findViewById(R.id.alarmSettings_time);
        setUpSection(timeSection, "Time", "Select a time...", 24, this::showTimePickerDialog);

        return view;
    }

    @Override
    public void onAlarmSubmit() {}

    /**
     * Create a UI section for the time settings tab. Sets the label, value, and an on click listener.
     */
    private void setUpSection(View section, String label, String defaultValue, int valueSize, OnClickListener onClickListener) {
        setSectionLabelText(section, label);
        setSectionValueText(section, defaultValue, valueSize);
        section.setOnClickListener(onClickListener);
    }

    /**
     * Show the time picker and pair the given view with the on click listener.
     */
    private void showTimePickerDialog(View view) {
        Bundle bundle = new Bundle();
        Optional.ofNullable(newAlarm.getHour()).ifPresent(hour -> bundle.putInt("hour", hour));
        Optional.ofNullable(newAlarm.getMinutes()).ifPresent(minute -> bundle.putInt("minute", minute));

        TimePickerFragment timePickerDialog = new TimePickerFragment(new OnSectionTimeSet(view, newAlarm));
        timePickerDialog.setArguments(bundle);
        timePickerDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
    }
}

package com.jesseoberstein.alert.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

import static java.util.Calendar.getInstance;

/**
 * A dialog fragment that shows a time picker.
 */
public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {
    private OnTimeSetListener onTimeSetListener;

    public TimePickerFragment() {}

    @SuppressLint("ValidFragment")
    public TimePickerFragment(OnTimeSetListener onTimeSetListener) {
        super();
        this.onTimeSetListener = onTimeSetListener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Try getting the time passed to the dialog (the current time set on the alarm).
        // If the alarm doesn't have a time, default to using the current time.
        int hour = getArguments().getInt("hour", calendar.get(Calendar.HOUR_OF_DAY));
        int minute = getArguments().getInt("minute", calendar.get(Calendar.MINUTE));

        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.onTimeSetListener.onTimeSet(timePicker, hour, minute);
    }
}

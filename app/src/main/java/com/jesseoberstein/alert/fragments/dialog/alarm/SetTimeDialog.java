package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmTimeBinding;
import com.jesseoberstein.alert.interfaces.AlarmTimeSetter;

/**
 * A dialog fragment that shows a time picker.
 */
public class SetTimeDialog extends AlarmModifierDialog {
    private AlarmTimeSetter alarmTimeSetter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmTimeSetter = (AlarmTimeSetter) getAlarmModifier();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AlarmTimeSetter");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlarmTimeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_time, null, false);
        binding.setAlarm(getDraftAlarm());

        View timePickerDialog = binding.getRoot();
        TimePicker timePicker = timePickerDialog.findViewById(R.id.alarm_time_picker);

        return new AlertDialog.Builder(getActivity())
                .setView(timePickerDialog)
                .setPositiveButton(R.string.ok, ((dialogInterface, i) -> this.onPositiveButtonClick(timePicker)))
                .setNegativeButton(R.string.cancel, ((dialogInterface, i) -> {}))
                .create();
    }

    public void onPositiveButtonClick(TimePicker timePicker) {
        this.alarmTimeSetter.onAlarmTimeSet(timePicker.getHour(), timePicker.getMinute());
    }

    @BindingAdapter({"hour", "minutes", "is24HourMode"})
    public static void setTime(TimePicker timePicker, Integer hour, Integer minutes, boolean is24HourMode) {
        timePicker.setHour(hour);
        timePicker.setMinute(minutes);
        timePicker.setIs24HourView(is24HourMode);
    }
}

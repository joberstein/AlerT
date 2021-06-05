package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmTimeBinding;
import com.jesseoberstein.alert.models.UserAlarm;

/**
 * A dialog fragment that shows a time picker.
 */
public class SetTimeDialog extends AlarmModifierDialog {

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlarmTimeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_time, null, false);
        this.viewModel.getDraftAlarm().observe(requireActivity(), binding::setAlarm);

        View timePickerDialog = binding.getRoot();
        TimePicker timePicker = timePickerDialog.findViewById(R.id.alarm_time_picker);

        return this.getAlertDialogBuilder()
                .setView(timePickerDialog)
                .setPositiveButton(R.string.ok, ((dialogInterface, i) -> this.onPositiveButtonClick(timePicker)))
                .setNegativeButton(R.string.cancel, ((dialogInterface, i) -> {}))
                .create();
    }

    public void onPositiveButtonClick(TimePicker timePicker) {
        UserAlarm newAlarm = this.userAlarm.toBuilder()
                .hour(timePicker.getHour())
                .minutes(timePicker.getMinute())
                .build();

        this.viewModel.getDraftAlarm().setValue(newAlarm);
    }

    @BindingAdapter({"hour", "minutes", "is24HourMode"})
    public static void setTime(TimePicker timePicker, Integer hour, Integer minutes, boolean is24HourMode) {
        timePicker.setHour(hour);
        timePicker.setMinute(minutes);
        timePicker.setIs24HourView(is24HourMode);
    }
}

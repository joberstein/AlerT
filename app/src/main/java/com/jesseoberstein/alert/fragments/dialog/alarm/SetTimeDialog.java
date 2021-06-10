package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.util.Optional;

/**
 * A dialog fragment that shows a time picker.
 */
public class SetTimeDialog extends AlarmModifierDialog {

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LocalTime now = LocalTime.now();

        int hour = Optional.ofNullable(this.viewModel.getHour().getValue())
                .orElseGet(() -> now.plusHours(1).getHour());

        int minutes = Optional.ofNullable(this.viewModel.getMinutes().getValue())
                .orElseGet(now::getMinute);

        Context context = this.getAlertDialogBuilder().getContext();

        return new TimePickerDialog(context, this::onTimeSet, hour, minutes, false);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.viewModel.getHour().setValue(hourOfDay);
        this.viewModel.getMinutes().setValue(minute);
    }
}

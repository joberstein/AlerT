package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.SelectedDays;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Arrays;

import static android.icu.text.DateFormatSymbols.getInstance;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;

/**
 * A dialog fragment that shows a dialog for setting custom days that the alarm should repeat.
 */
public class SetDaysDialog extends AlarmModifierDialog {

    private SelectedDays selectedDays;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.selectedDays = this.userAlarm.getSelectedDays();

        String[] daysList = Arrays.copyOfRange(getInstance().getWeekdays(), SUNDAY, SATURDAY + 1);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.repeat_custom_dialog_title)
                .setMultiChoiceItems(daysList, this.selectedDays.toBooleanArray(), this::onItemToggle)
                .setPositiveButton(R.string.ok, this::onPositiveButtonClick)
                .setNegativeButton(R.string.cancel, this::onNegativeButtonClick)
                .create();
    }

    private void onItemToggle(DialogInterface dialogInterface, int toggleIndex, boolean isChecked) {
        this.selectedDays = this.selectedDays.update(toggleIndex, isChecked);
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface, int i) {
        UserAlarm newAlarm = this.userAlarm.toBuilder()
                .repeatType(RepeatType.CUSTOM)
                .selectedDays(this.selectedDays)
                .build();

        this.viewModel.getDraftAlarm().setValue(newAlarm);
    }

    private void onNegativeButtonClick(DialogInterface dialogInterface, int i) {}
}

package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.SelectedDays;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * A dialog fragment that shows a dialog for setting custom days that the alarm should repeat.
 */
public class SetDaysDialog extends AlarmModifierDialog {

    private SelectedDays selectedDays;

    private static final String[] DAYS_LIST = Arrays.stream(DayOfWeek.values())
            .map(dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US))
            .toArray(String[]::new);

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Optional.ofNullable(this.viewModel.getSelectedDays().getValue())
                .ifPresent(selectedDays -> this.selectedDays = selectedDays);

        return this.getAlertDialogBuilder()
                .setTitle(R.string.repeat_custom_dialog_title)
                .setMultiChoiceItems(DAYS_LIST, this.selectedDays.toBooleanArray(), this::onItemToggle)
                .setPositiveButton(R.string.ok, this::onPositiveButtonClick)
                .setNegativeButton(R.string.cancel, this::onNegativeButtonClick)
                .create();
    }

    private void onItemToggle(DialogInterface dialogInterface, int toggleIndex, boolean isChecked) {
        DayOfWeek day = DayOfWeek.of(toggleIndex + 1);
        this.selectedDays = this.selectedDays.update(day, isChecked);
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface, int i) {
        this.viewModel.getRepeatType().setValue(RepeatType.CUSTOM);
        this.viewModel.getSelectedDays().setValue(this.selectedDays);
    }

    private void onNegativeButtonClick(DialogInterface dialogInterface, int i) {}
}

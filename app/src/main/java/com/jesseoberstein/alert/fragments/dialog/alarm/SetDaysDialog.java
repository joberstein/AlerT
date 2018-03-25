package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.AlarmDaySetter;

import java.util.Arrays;
import java.util.stream.IntStream;

import static android.icu.text.DateFormatSymbols.getInstance;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;

/**
 * A dialog fragment that shows a dialog for setting custom days that the alarm should repeat.
 */
public class SetDaysDialog extends AlarmModifierDialog {
    private AlarmDaySetter alarmDaySetter;
    private int[] selectedDays;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmDaySetter = (AlarmDaySetter) getAlarmModifier();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AlarmDaySetter");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.selectedDays = getDraftAlarm().getWeekdays();

        String[] daysList = Arrays.copyOfRange(getInstance().getWeekdays(), SUNDAY, SATURDAY + 1);
        int numDays = daysList.length;
        boolean[] convertedDays = new boolean[numDays];
        IntStream.range(0, numDays).forEach(i -> convertedDays[i] = (this.selectedDays[i] == 1));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.repeat_custom_dialog_title)
                .setMultiChoiceItems(daysList, convertedDays, this::onItemToggle)
                .setPositiveButton(R.string.ok, this::onPositiveButtonClick)
                .setNegativeButton(R.string.cancel, this::onNegativeButtonClick)
                .create();
    }

    private void onItemToggle(DialogInterface dialogInterface, int toggleIndex, boolean isChecked) {
        this.selectedDays[toggleIndex] = isChecked ? 1 : 0;
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface, int i) {
        Log.v("Days to set", Arrays.toString(this.selectedDays));
        this.alarmDaySetter.onAlarmDaysSet(this.selectedDays);
    }

    private void onNegativeButtonClick(DialogInterface dialogInterface, int i) {}
}

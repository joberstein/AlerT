package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jesseoberstein.alert.interfaces.AlarmRepeatSetter;
import com.jesseoberstein.alert.models.RepeatType;

import static com.jesseoberstein.alert.models.RepeatType.*;

/**
 * A dialog fragment that shows a dialog for setting the alarm repeat type.
 */
public class SetRepeatTypeDialog extends AlarmModifierDialog {
    private AlarmRepeatSetter alarmRepeatSetter;
    private int previousRepeatType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmRepeatSetter = (AlarmRepeatSetter) getAlarmModifier();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AlarmRepeatSetter");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.previousRepeatType = getDraftAlarm().getRepeatType().ordinal();

        return new AlertDialog.Builder(getActivity())
                .setTitle("Set Repeat Type")
                .setSingleChoiceItems(getRepeatTypes(), this.previousRepeatType, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        RepeatType selected = values()[selectedIndex];
        RepeatType previous = values()[this.previousRepeatType];
        this.alarmRepeatSetter.onAlarmRepeatSet(selected, previous);
        new android.os.Handler().postDelayed(dialogInterface::dismiss,500);
    }
}

package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.AlarmRepeatSetter;
import com.jesseoberstein.alert.models.RepeatType;

import static com.jesseoberstein.alert.models.RepeatType.getRepeatTypes;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for setting the alarm repeat type.
 */
public class SetRepeatTypeDialog extends AlarmModifierDialog {
    private AlarmRepeatSetter alarmRepeatSetter;

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
        int currentRepeatType = getDraftAlarm().getRepeatType().getId();

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.repeat_dialog_title)
                .setSingleChoiceItems(getRepeatTypes(), currentRepeatType, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        this.alarmRepeatSetter.onAlarmRepeatSet(RepeatType.values()[selectedIndex]);
        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}

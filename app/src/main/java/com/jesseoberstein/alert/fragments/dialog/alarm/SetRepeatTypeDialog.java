package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.RepeatType;

import static com.jesseoberstein.alert.models.RepeatType.getRepeatTypes;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for setting the alarm repeat type.
 */
public class SetRepeatTypeDialog extends AlarmModifierDialog {

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        int currentRepeatType = this.userAlarm.getRepeatType().getId();

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.repeat_dialog_title)
                .setSingleChoiceItems(getRepeatTypes(), currentRepeatType, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        RepeatType repeatType = RepeatType.values()[selectedIndex];

        if (RepeatType.CUSTOM.equals(repeatType)) {
            new SetDaysDialog().show(this.getParentFragmentManager(), "setDays");
        }

        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}

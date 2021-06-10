package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Optional;

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

        int repeatTypeId = Optional.ofNullable(this.viewModel.getRepeatType().getValue())
                .map(RepeatType::getId)
                .orElse(-1);

        return this.getAlertDialogBuilder()
                .setTitle(R.string.repeat_dialog_title)
                .setSingleChoiceItems(getRepeatTypes(), repeatTypeId, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        RepeatType repeatType = RepeatType.values()[selectedIndex];

        if (RepeatType.CUSTOM.equals(repeatType)) {
            new SetDaysDialog().show(this.getParentFragmentManager(), "setDays");
        } else {
            this.viewModel.getRepeatType().setValue(repeatType);
            this.viewModel.getSelectedDays().setValue(repeatType.getSelectedDays());
        }

        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}

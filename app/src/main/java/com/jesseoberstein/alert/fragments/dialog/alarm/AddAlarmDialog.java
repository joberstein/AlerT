package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.Dialog;
import android.os.Bundle;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.fragments.dialog.AbstractDialog;
import com.jesseoberstein.alert.utils.Constants;

import static com.jesseoberstein.alert.utils.Constants.*;

public class AddAlarmDialog extends AbstractDialog {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean isUpdate = getArguments().getBoolean(IS_UPDATE);
        int message = isUpdate ? R.string.update_alarm_dialog : R.string.add_alarm_dialog;
        int positiveText = isUpdate ? R.string.update : R.string.add;

        return this.createRouteDialogBuilder(savedInstanceState)
                .setMessage(getResources().getString(message, getAlarmName()))
                .setPositiveButton(positiveText, (dialog, id) -> getClickListener().onAddSelected(getArguments()))
                .create();
    }
}

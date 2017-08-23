package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.Dialog;
import android.os.Bundle;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.fragments.dialog.AbstractDialog;

public class RemoveAlarmDialog extends AbstractDialog {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return this.createRouteDialogBuilder(savedInstanceState)
                .setMessage(getResources().getString(R.string.remove_alarm_dialog, getAlarmName()))
                .setPositiveButton(R.string.remove, (dialog, id) -> getClickListener().onRemoveSelected(getArguments()))
                .create();
    }
}

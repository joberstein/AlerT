package com.jesseoberstein.alert.fragments.dialog.route;

import android.app.Dialog;
import android.os.Bundle;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.fragments.dialog.AbstractDialog;

public class RemoveRouteDialog extends AbstractDialog {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return this.createRouteDialogBuilder(savedInstanceState)
                .setMessage(getResources().getString(R.string.remove_route_dialog, getRouteName()))
                .setPositiveButton(R.string.remove, (dialog, id) -> getClickListener().onRemoveSelected(getArguments()))
                .create();
    }
}

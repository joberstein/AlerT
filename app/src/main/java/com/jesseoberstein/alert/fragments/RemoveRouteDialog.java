package com.jesseoberstein.alert.fragments;

import android.app.Dialog;
import android.os.Bundle;

import com.jesseoberstein.alert.R;

public class RemoveRouteDialog extends AbstractRouteDialog {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return this.createRouteDialogBuilder(savedInstanceState)
                .setMessage(getResources().getString(R.string.remove_route_dialog, getRouteName()))
                .setPositiveButton(R.string.remove, (dialog, id) ->
                        getClickListener().onRemoveSelectedRoute(getSelectedRoute()))
                .create();
    }
}

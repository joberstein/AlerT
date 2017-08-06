package com.jesseoberstein.alert.fragments;

import android.app.Dialog;
import android.os.Bundle;

import com.jesseoberstein.alert.R;

public class AddRouteDialog extends AbstractRouteDialog {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return this.createRouteDialogBuilder(savedInstanceState)
                .setMessage(getResources().getString(R.string.add_route_dialog, getRouteName()))
                .setPositiveButton(R.string.add, (dialog, id) ->
                        getClickListener().onAddSelectedRoute(getSelectedRoute()))
                .create();
    }
}

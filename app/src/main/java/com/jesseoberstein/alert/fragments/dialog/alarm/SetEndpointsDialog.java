package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.AlarmEndpointSetter;
import com.jesseoberstein.alert.interfaces.data.EndpointsReceiver;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.utils.EndpointsUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A dialog fragment that shows a dialog for setting custom days that the alarm should repeat.
 */
public class SetEndpointsDialog extends AlarmModifierDialog {

    @Inject
    AlarmEndpointSetter alarmEndpointSetter;

    @Inject
    EndpointsReceiver endpointsReceiver;

    private List<Endpoint> selectedEndpoints = new ArrayList<>();
    private List<Endpoint> endpoints;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.endpoints = this.endpointsReceiver.getEndpointList();
        List<Endpoint> currentEndpoints = this.getDraftAlarmWithRelations().getEndpoints();
        this.selectedEndpoints.addAll(currentEndpoints);

        String[] endpointNames = EndpointsUtils.toStringArray(this.endpoints);
        boolean[] convertedEndpoints = EndpointsUtils.toBooleanArray(this.endpoints, this.selectedEndpoints);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.endpoints_dialog_title)
                .setMultiChoiceItems(endpointNames, convertedEndpoints, this::onItemToggle)
                .setPositiveButton(R.string.ok, this::onPositiveButtonClick)
                .setNegativeButton(R.string.cancel, this::onNegativeButtonClick)
                .create();
    }

    private void onItemToggle(DialogInterface dialogInterface, int toggleIndex, boolean isChecked) {
        if (isChecked) {
            this.selectedEndpoints.add(this.endpoints.get(toggleIndex));
        } else {
            this.selectedEndpoints.remove(this.endpoints.get(toggleIndex));
        }
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface, int i) {
        List<Endpoint> selectedEndpoints = EndpointsUtils.sort(this.selectedEndpoints);
        this.alarmEndpointSetter.onAlarmEndpointsSet(selectedEndpoints);
    }

    private void onNegativeButtonClick(DialogInterface dialogInterface, int i) {}
}

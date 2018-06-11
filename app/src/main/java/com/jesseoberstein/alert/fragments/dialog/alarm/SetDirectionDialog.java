package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.AlarmDirectionSetter;
import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.List;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm direction.
 */
public class SetDirectionDialog extends AlarmModifierDialog {
    private AlarmDirectionSetter alarmDirectionSetter;
    private DirectionsReceiver directionsReceiver;
    private List<Direction> directions;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmDirectionSetter = (AlarmDirectionSetter) getAlarmModifier();
            this.directionsReceiver = (DirectionsReceiver) context;
        } catch (ClassCastException e) {
            String msg = context + " must implement both AlarmDirectionSetter and DirectionsReceiver.";
            throw new ClassCastException(msg);
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.directions = this.directionsReceiver.getDirectionList();
        int currentDirection = Long.valueOf(getDraftAlarm().getDirectionId()).intValue();
        String[] directionNames = this.directions.stream().map(Direction::toString).toArray(String[]::new);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.direction_dialog_title)
                .setSingleChoiceItems(directionNames, currentDirection, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        Direction selectedDirection = this.directions.get(selectedIndex);
        if (!selectedDirection.equals(getDraftAlarm().getDirection())) {
            this.alarmDirectionSetter.onAlarmDirectionSet(selectedDirection);
        }
        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}

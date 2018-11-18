package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.AlarmDirectionSetter;
import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm direction.
 */
public class SetDirectionDialog extends AlarmModifierDialog {

    @Inject
    AlarmDirectionSetter alarmDirectionSetter;

    @Inject
    DirectionsReceiver directionsReceiver;

    private List<Direction> directions;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.directions = this.directionsReceiver.getDirectionList();
        Direction selectedDirection = this.getDraftAlarmWithRelations().getDirection();
        int selectedId = Optional.ofNullable(selectedDirection).map(Direction::getDirectionId).orElse(-1);
        String[] directionNames = this.directions.stream().map(Direction::toString).toArray(String[]::new);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.direction_dialog_title)
                .setSingleChoiceItems(directionNames, selectedId, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        Direction selectedDirection = this.directions.get(selectedIndex);
        if (!selectedDirection.equals(getDraftAlarmWithRelations().getDirection())) {
            this.alarmDirectionSetter.onAlarmDirectionSet(selectedDirection);
        }
        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}

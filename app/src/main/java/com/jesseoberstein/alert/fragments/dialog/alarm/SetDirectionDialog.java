package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.List;

import javax.inject.Inject;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm direction.
 */
public class SetDirectionDialog extends AlarmModifierDialog {

    @Inject
    DirectionsReceiver directionsReceiver;

    private List<Direction> directions;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.directions = this.directionsReceiver.getDirectionList();

//        int selectedId = Optional.ofNullable(userAlarm.getDirectionId()).orElse(-1);
        int selectedId = Long.valueOf(userAlarm.getDirectionId()).intValue();
        String[] directionNames = this.directions.stream().map(Direction::toString).toArray(String[]::new);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.direction_dialog_title)
                .setSingleChoiceItems(directionNames, selectedId, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        Direction selectedDirection = this.directions.get(selectedIndex);

        if (selectedDirection.getId() != this.userAlarm.getDirectionId()) {
            UserAlarm newAlarm = this.userAlarm.withDirectionId(selectedDirection.getId());
            this.viewModel.getDraftAlarm().setValue(newAlarm);
            this.viewModel.getDirection().setValue(selectedDirection);
        }

        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}

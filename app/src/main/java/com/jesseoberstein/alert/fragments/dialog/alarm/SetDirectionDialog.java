package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm direction.
 */
@RequiredArgsConstructor
public class SetDirectionDialog extends AlarmModifierDialog {

    private final List<Direction> directions;
    private Direction selectedDirection;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        this.viewModel.getDirection().observe(requireActivity(), direction -> {
            this.selectedDirection = direction;
        });

        int selectedDirectionId = Optional.ofNullable(selectedDirection)
                .map(Direction::getDirectionId)
                .orElse(-1);

        String[] directionNames = this.directions.stream()
                .map(Direction::toString)
                .toArray(String[]::new);

        return this.getAlertDialogBuilder()
                .setTitle(R.string.direction_dialog_title)
                .setSingleChoiceItems(directionNames, selectedDirectionId, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        Direction selectedDirection = this.directions.get(selectedIndex);
        this.viewModel.getDirection().setValue(selectedDirection);

        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}

package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.AlarmDirectionSetter;
import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;
import static com.jesseoberstein.alert.utils.Constants.DIRECTIONS;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

/**
 * A dialog fragment that shows a dialog for selecting the alarm direction.
 */
public class SetDirectionDialog extends AlarmModifierDialog {
    private AlarmDirectionSetter alarmDirectionSetter;
    private List<Direction> directions;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmDirectionSetter = (AlarmDirectionSetter) getAlarmModifier();
            List<Direction> parcelDirections = getArguments().getParcelableArrayList(DIRECTIONS);
            this.directions = parcelDirections.stream().sorted(comparingInt(Direction::getId)).collect(toList());
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AlarmDirectionSetter");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        int currentDirection = getDraftAlarm().getDirectionId();
        String[] directionNames = this.directions.stream().map(Direction::toString).toArray(String[]::new);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.direction_dialog_title)
                .setSingleChoiceItems(directionNames, currentDirection, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        this.alarmDirectionSetter.onAlarmDirectionSet(this.directions.get(selectedIndex));
        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}

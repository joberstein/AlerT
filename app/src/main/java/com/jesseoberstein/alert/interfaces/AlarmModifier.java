package com.jesseoberstein.alert.interfaces;

import com.google.android.material.snackbar.Snackbar;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

/**
 * An implementing activity should provide access to a draft alarm.
 */

public interface AlarmModifier {
    UserAlarm getDraftAlarm();
    UserAlarmWithRelations getDraftAlarmWithRelations();
    Snackbar getValidationSnackbar();
}

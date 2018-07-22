package com.jesseoberstein.alert.interfaces;

import android.support.design.widget.Snackbar;

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

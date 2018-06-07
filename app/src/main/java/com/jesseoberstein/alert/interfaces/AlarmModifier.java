package com.jesseoberstein.alert.interfaces;

import android.support.design.widget.Snackbar;

import com.jesseoberstein.alert.models.UserAlarm;

/**
 * An implementing activity should provide access to a draft alarm.
 */

public interface AlarmModifier {
    UserAlarm getDraftAlarm();
    Snackbar getValidationSnackbar();
}

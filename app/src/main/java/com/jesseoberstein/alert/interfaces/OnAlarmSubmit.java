package com.jesseoberstein.alert.interfaces;

import android.os.Bundle;

/**
 * Fragments of EditAlarm should implement this method and return a bundle with user-entered info.
 */
public interface OnAlarmSubmit {
    public Bundle onAlarmSubmit();
}

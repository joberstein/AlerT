package com.jesseoberstein.alert.listeners.alarm;

import android.view.View;

import com.jesseoberstein.alert.models.UserAlarm;

/**
 * An abstract listener that can be used for 'section' event callbacks. Provides the clicked section
 * and the current user alarm.
 */
public abstract class OnSectionAction {
    private View section;
    private UserAlarm alarm;

    OnSectionAction(View section, UserAlarm alarm) {
        this.section = section;
        this.alarm = alarm;
    }

    View getSection() {
        return this.section;
    }

    public UserAlarm getAlarm() {
        return this.alarm;
    }
}

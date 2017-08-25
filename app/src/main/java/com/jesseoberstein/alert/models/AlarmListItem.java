package com.jesseoberstein.alert.models;

import com.jesseoberstein.alert.R;

/**
 * A wrapper class for building a custom list item; targeted towards list of alarms.
 */
public class AlarmListItem extends CustomListItem {

    /**
     * Build an alarm list item with the given list item.
     */
    public static CustomListItem buildAlarmListItem(UserAlarm alarm, String endpoints) {
        int icon = alarm.isActive() ? R.drawable.circle_light_green : R.drawable.circle_light_gray;

        return new CustomListItem()
                .withId(alarm.getId())
                .withIcon(icon)
                .withPrimaryText(alarm.getNickname())
                .withSecondaryText(alarm.getStation())
                .withTertiaryText(endpoints)
                .withChevron(R.drawable.ic_chevron_right);
    }
}

package com.jesseoberstein.alert.listeners.alarm;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.fragments.dialog.alarm.AddAlarmDialog;

import static android.view.View.OnClickListener;
import static com.jesseoberstein.alert.utils.Constants.ALARM_DAYS;
import static com.jesseoberstein.alert.utils.Constants.ALARM_SETTINGS;
import static com.jesseoberstein.alert.utils.Constants.ALARM_TIME;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.IS_UPDATE;
import static com.jesseoberstein.alert.utils.Constants.STATION;

public class UpdateAlarmOnClick implements OnClickListener {
    public static final String NEW_ALARM = "newAlarm";
    private EditAlarm activity;

    public UpdateAlarmOnClick(EditAlarm activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        AlarmPagerAdapter adapter = this.activity.getAlarmPagerAdapter();

        Bundle alarmBundle = new Bundle();
        alarmBundle.putString(STATION, this.activity.getStation());
        alarmBundle.putBundle(ALARM_TIME, adapter.getTimeFragment().onAlarmSubmit());
        alarmBundle.putBundle(ALARM_DAYS, adapter.getDayFragment().onAlarmSubmit());
        alarmBundle.putBundle(ALARM_SETTINGS, adapter.getSettingsFragment().onAlarmSubmit());
        alarmBundle.putStringArrayList(ENDPOINTS, this.activity.getEndpoints());

        Bundle newAlarmBundle = new Bundle();
        newAlarmBundle.putBundle(NEW_ALARM, alarmBundle);
        newAlarmBundle.putBoolean(IS_UPDATE, this.activity.isUpdate());

        DialogFragment dialog = new AddAlarmDialog();
        dialog.setArguments(newAlarmBundle);
        dialog.show(this.activity.getFragmentManager(), "AddAlarmDialog");
    }
}

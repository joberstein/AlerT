package com.jesseoberstein.alert.listeners.alarm;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.fragments.dialog.alarm.AddAlarmDialog;

import static android.view.View.OnClickListener;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;

public class UpdateAlarmOnClick implements OnClickListener {
    private EditAlarm activity;

    public UpdateAlarmOnClick(EditAlarm activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        AlarmPagerAdapter adapter = this.activity.getAlarmPagerAdapter();
        adapter.getTimeFragment().onAlarmSubmit();
        adapter.getDayFragment().onAlarmSubmit();
        adapter.getSettingsFragment().onAlarmSubmit();

        Bundle alarmBundle = new Bundle();
        alarmBundle.putSerializable(ALARM, this.activity.getAlarm());
        alarmBundle.putStringArrayList(ENDPOINTS, this.activity.getEndpoints());

        DialogFragment dialog = new AddAlarmDialog();
        dialog.setArguments(alarmBundle);
        dialog.show(this.activity.getFragmentManager(), "AddAlarmDialog");
    }
}

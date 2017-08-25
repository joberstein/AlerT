package com.jesseoberstein.alert.listeners.alarm;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.fragments.dialog.alarm.AddAlarmDialog;
import com.jesseoberstein.alert.models.UserAlarm;

import static android.view.View.OnClickListener;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.IS_UPDATE;

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

        Bundle newAlarmBundle = new Bundle();
        newAlarmBundle.putParcelable(ALARM, this.activity.getAlarm());
        newAlarmBundle.putStringArrayList(ENDPOINTS, this.activity.getEndpoints());
        newAlarmBundle.putBoolean(IS_UPDATE, this.activity.isUpdate());

        DialogFragment dialog = new AddAlarmDialog();
        dialog.setArguments(newAlarmBundle);
        dialog.show(this.activity.getFragmentManager(), "AddAlarmDialog");
    }
}

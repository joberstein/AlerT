package com.jesseoberstein.alert.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.data.dao.RouteDao;
import com.jesseoberstein.alert.data.dao.StopDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.databinding.MbtaSettingsBinding;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.tasks.QueryRoutesTask;
import com.jesseoberstein.alert.tasks.QueryStopsTask;

public class MbtaSettingsFragment extends AlarmSettingsFragment implements OnAlarmSubmit {
    private UserAlarm newAlarm;
    private FragmentManager fragmentManager;

    public static MbtaSettingsFragment newInstance(int page) {
        return (MbtaSettingsFragment) AlarmSettingsFragment.newInstance(page, new MbtaSettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.newAlarm = ((EditAlarm) getActivity()).getDraftAlarm();
        this.fragmentManager = getActivity().getSupportFragmentManager();

        MbtaSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_tab_mbta, container, false);
        binding.setAlarm(this.newAlarm);

        View view = binding.getRoot();
        view.findViewById(R.id.alarmSettings_route).setOnClickListener(this::showRouteDialog);
        view.findViewById(R.id.alarmSettings_stop).setOnClickListener(this::showStopDialog);

        return view;
    }

    @Override
    public void onAlarmSubmit() {}

    private void showRouteDialog(View view) {
        RouteDao routeDao = AppDatabase.getInstance(getContext()).routeDao();
        new QueryRoutesTask(routeDao, this.fragmentManager).execute();
    }

    private void showStopDialog(View view) {
        StopDao stopDao = AppDatabase.getInstance(getContext()).stopDao();
        new QueryStopsTask(stopDao, this.fragmentManager).execute(this.newAlarm.getRoute().getId());
    }

    private void showDialogFragment(DialogFragment dialog, String tagName) {
        dialog.show(this.fragmentManager, tagName);
    }
}

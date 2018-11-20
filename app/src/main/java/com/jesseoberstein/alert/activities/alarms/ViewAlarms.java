package com.jesseoberstein.alert.activities.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.activities.base.BaseActivity;
import com.jesseoberstein.alert.adapters.AlarmsAdapter;
import com.jesseoberstein.alert.adapters.EmptyRecyclerViewObserver;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.AlarmAdapterInteractor;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.tasks.DeleteAlarmTask;
import com.jesseoberstein.alert.tasks.QueryAlarmsTask;
import com.jesseoberstein.alert.tasks.UpdateAlarmTask;
import com.jesseoberstein.alert.utils.ActivityUtils;
import com.jesseoberstein.alert.utils.AlarmManagerHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import static com.jesseoberstein.alert.utils.Constants.ALARM;

public class ViewAlarms extends BaseActivity implements OnDialogClick, AlarmReceiver, AlarmAdapterInteractor {

    @Inject
    ActionBar actionBar;

    @Inject
    AppDatabase database;

    @Inject
    UserAlarmScheduler userAlarmScheduler;

    @Inject
    AlarmManagerHelper alarmManagerHelper;

    private AlarmsAdapter alarmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_view_alarms);
        createQueryAlarmsTask().execute();

        Optional.ofNullable(this.actionBar).ifPresent(bar -> {
            bar.setTitle(R.string.view_alarms_page);
            bar.setDisplayHomeAsUpEnabled(false);
        });

        // Start 'EditAlarm' when the floating action button is clicked.
        findViewById(R.id.add_alarm).setOnClickListener(this.onCreateAlarmClick());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarms_options, menu);
        ActivityUtils.setDrawableColor(this, menu.getItem(0).getIcon(), R.attr.menuItemIconColor);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        createQueryAlarmsTask().execute();
    }

    @Override
    public void onRemoveSelected(Bundle bundle) {
        UserAlarmWithRelations alarm = (UserAlarmWithRelations) bundle.getSerializable(ALARM);
        new DeleteAlarmTask(this, database).execute(alarm);
    }

    @Override
    public void onAddSelected(Bundle selected) { }

    @Override
    public void onCancelSelected(Bundle selected) { }

    @Override
    public void onReceiveAlarms(List<UserAlarmWithRelations> alarms) {
        if (this.alarmsAdapter == null) {
            RecyclerView alarmsView = findViewById(R.id.alarm_list);
            this.alarmsAdapter = new AlarmsAdapter(R.layout.list_alarms, new ArrayList<>());
            this.alarmsAdapter.setHasStableIds(true);
            alarmsView.setAdapter(this.alarmsAdapter);
            alarmsView.setLayoutManager(new GridLayoutManager(this, 1));

            // For some reason, the adapter needs to be set first.
            this.alarmsAdapter.registerAdapterDataObserver(new EmptyRecyclerViewObserver(alarmsView, findViewById(R.id.alarm_list_empty)));
        }

        // Create a list of existing and new alarms based on the queried alarms.
        Map<Boolean, List<UserAlarmWithRelations>> alarmMap = alarms.stream()
                .collect(Collectors.groupingBy(this.alarmsAdapter.getAlarms()::contains));

        // If the alarms are already in the adapter, update them.
        Optional.ofNullable(alarmMap.get(true))
                .ifPresent(existingAlarms -> existingAlarms.forEach(this.alarmsAdapter::updateItem));

        // If the alarms are not already in the adapter, add them.
        Optional.ofNullable(alarmMap.get(false))
                .ifPresent(newAlarms -> newAlarms.forEach(this.alarmsAdapter::addItem));
    }

    @Override
    public void onDeleteAlarm(UserAlarmWithRelations alarm) {
        alarmsAdapter.removeItem(alarm);
        alarmManagerHelper.cancelUserAlarm(alarm);
    }

    @Override
    public void onUpdateAlarm(UserAlarmWithRelations alarm) {
        alarmsAdapter.updateItem(alarm);

        if (alarm.getAlarm().isActive()) {
            alarmManagerHelper.scheduleUserAlarm(alarm);
        } else {
            alarmManagerHelper.cancelUserAlarm(alarm);
        }
    }

    @Override
    public void onInsertAlarm(long insertedAlarmId) {}

    @VisibleForTesting
    public View.OnClickListener onCreateAlarmClick() {
        return new StartActivityOnClick(this, EditAlarm.class)
                .withAction(Intent.ACTION_INSERT);
    }

    private QueryAlarmsTask createQueryAlarmsTask() {
        return new QueryAlarmsTask(this, database, userAlarmScheduler);
    }

    @Override
    public void onAlarmStatusToggle(UserAlarmWithRelations alarmWithRelations) {
        new UpdateAlarmTask(this, database).execute(alarmWithRelations);
    }
}

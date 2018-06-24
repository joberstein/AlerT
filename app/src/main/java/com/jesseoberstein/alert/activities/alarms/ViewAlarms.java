package com.jesseoberstein.alert.activities.alarms;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.activities.base.DatabaseActivity;
import com.jesseoberstein.alert.adapters.AlarmsAdapter;
import com.jesseoberstein.alert.adapters.EmptyRecyclerViewObserver;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.tasks.QueryAlarmsTask;
import com.jesseoberstein.alert.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViewAlarms extends DatabaseActivity implements OnDialogClick, AlarmReceiver {
    private AlarmsAdapter alarmsAdapter;
    private AlarmManager alarmManager;
    private List<UserAlarm> alarms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_view_alarms);
        new QueryAlarmsTask(this, getDatabase(this)).execute();

        Optional.ofNullable(getSupportActionBar()).ifPresent(bar -> {
            bar.setTitle(R.string.view_alarms_page);
            bar.setDisplayHomeAsUpEnabled(false);
        });

        // Start 'EditAlarm' when the floating action button is clicked.
        findViewById(R.id.add_alarm).setOnClickListener(this.getOnCreateAlarmClick());

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarms_options, menu);
        ActivityUtils.setIconColor(this, menu.getItem(0).getIcon(), R.attr.menuItemIconColor);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new QueryAlarmsTask(this, getDatabase(this)).execute();
    }

    @Override
    public void onRemoveSelected(Bundle selected) {
//        try {
//            deleteAlarm(selected.getInt(ALARM_ID));
//        } catch (SQLException e) {
//            Toast.makeText(this, "Could not delete the selected alarm.", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onAddSelected(Bundle selected) { }

    @Override
    public void onCancelSelected(Bundle selected) { }

    @Override
    public void onReceiveAlarms(List<UserAlarm> alarms) {
        List<UserAlarm> alarmsToAdd = alarms.stream()
                .filter(alarm -> !this.alarms.contains(alarm))
                .collect(Collectors.toList());

        this.alarms.addAll(alarmsToAdd);
        if (this.alarmsAdapter == null) {
            RecyclerView alarmsView = findViewById(R.id.alarm_list);
            this.alarmsAdapter = new AlarmsAdapter(R.layout.list_alarms, new ArrayList<>(this.alarms));
            this.alarmsAdapter.setHasStableIds(true);
            alarmsView.setAdapter(this.alarmsAdapter);
            alarmsView.setLayoutManager(new GridLayoutManager(this, 1));

            // For some reason, the adapter needs to be set first.
            this.alarmsAdapter.registerAdapterDataObserver(new EmptyRecyclerViewObserver(alarmsView, findViewById(R.id.alarm_list_empty)));
        } else {
            alarmsToAdd.forEach(this.alarmsAdapter::addItem);
        }
    }

    @Override
    public void onInsertAlarm(long insertedAlarmId) {}

    @VisibleForTesting
    public View.OnClickListener getOnCreateAlarmClick() {
        return new StartActivityOnClick(this, EditAlarm.class)
                .withAction(Intent.ACTION_INSERT);
    }
}

package com.jesseoberstein.alert.activities.alarms;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.CreateAlarm;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.data.UserAlarmDao;
import com.jesseoberstein.alert.data.UserEndpointDao;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.listeners.alarm.RemoveAlarmOnLongClick;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserEndpoint;
import com.jesseoberstein.alert.models.UserRoute;
import com.jesseoberstein.alert.providers.StationsProvider;
import com.jesseoberstein.alert.utils.AlarmUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.models.AlarmListItem.buildAlarmListItem;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;

public class ViewAlarms extends AppCompatActivity implements OnDialogClick {
    private static String selectedRoute;
    private static int themeColor;
    private CustomListAdapter myAlarmsAdapter;
    private RuntimeExceptionDao<UserAlarm, Integer> userAlarmDao;
    private RuntimeExceptionDao<UserEndpoint, Integer> userEndpointDao;
    private StationsProvider stationsProvider;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userAlarmDao = new UserAlarmDao(getApplicationContext()).getDao();
        this.userEndpointDao = new UserEndpointDao(getApplicationContext()).getDao();
        stationsProvider = StationsProvider.init(getAssets());

        setContentView(R.layout.activities_view_alarms);
        Bundle selectedBundle = getIntent().getExtras();
        selectedRoute = selectedBundle.getString(ROUTE);
        themeColor = selectedBundle.getInt(COLOR);

        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(selectedRoute);
            bar.setBackgroundDrawable(getDrawable(themeColor));
            bar.setDisplayHomeAsUpEnabled(true);
        });

        List<UserAlarm> routeAlarms = userAlarmDao.queryForEq("route_id", selectedRoute);
        ArrayList<CustomListItem> userAlarms = routeAlarms.stream()
                .map(alarm -> {
                    String endpoints = userEndpointDao.queryForEq("alarm_id", alarm.getId()).stream()
                            .map(UserEndpoint::getEndpointName)
                            .collect(Collectors.joining(", "));
                    return buildAlarmListItem(alarm, endpoints);
                })
                .collect(Collectors.toCollection(ArrayList::new));
        myAlarmsAdapter = new CustomListAdapter(this, R.layout.list_alarms, userAlarms);

        ListView listView = (ListView) findViewById(R.id.alarm_list);
        listView.setAdapter(myAlarmsAdapter);
        listView.setEmptyView(findViewById(R.id.alarm_list_empty));
        listView.setOnItemClickListener(new StartActivityOnClick(this, EditAlarm.class)
                .withBundle(selectedBundle)
                .withRequestCode(EditAlarm.REQUEST_CODE));
        listView.setOnItemLongClickListener(new RemoveAlarmOnLongClick(this));

        FloatingActionButton addAlarmView = (FloatingActionButton) findViewById(R.id.add_alarm);
        addAlarmView.setBackgroundTintList(getColorStateList(themeColor));
        addAlarmView.setOnClickListener(new StartActivityOnClick(this, CreateAlarm.class)
                .withAction(Intent.ACTION_INSERT)
                .withBundle(selectedBundle)
                .withRequestCode(CreateAlarm.REQUEST_CODE));

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarms_options, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            UserAlarm alarm = (UserAlarm) data.getExtras().getSerializable(ALARM);
            alarm.setRoute(new UserRoute(selectedRoute));

            switch (requestCode) {
                case CreateAlarm.REQUEST_CODE:
                    List<String> endpoints = data.getExtras().getStringArrayList(ENDPOINTS);
                    createAlarm(alarm, endpoints);
                    break;

                case EditAlarm.REQUEST_CODE:
                    updateAlarm(alarm);
            }
        }
    }

    @Override
    public void onRemoveSelected(Bundle selected) {
        int alarmId = selected.getInt(ALARM_ID);
        DeleteBuilder<UserEndpoint, Integer> endpointDeleter = userEndpointDao.deleteBuilder();

        try {
            endpointDeleter.where().eq("alarm_id", alarmId);
            endpointDeleter.delete();
            userAlarmDao.deleteById(alarmId);
            myAlarmsAdapter.removeItemById(alarmId);
        } catch (SQLException e) {
            Toast.makeText(this, "Could not delete the selected alarm.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddSelected(Bundle selected) { }

    @Override
    public void onCancelSelected(Bundle selected) { }

    public RuntimeExceptionDao<UserAlarm, Integer> getUserAlarmDao() {
        return userAlarmDao;
    }

    private String[] getAlarmStopIds(UserAlarm alarm) {
        return stationsProvider.getStopIdForStopAndDirection(alarm.getStation(), alarm.getDirection(), selectedRoute)
                .toArray(new String[]{});
    }

    /**
     * Persist a new alarm and add a new alarm to the alarms list with the given endpoints.
     * @param alarm The given new alarm.
     * @param endpoints A list of the alarm's endpoints.
     */
    private void createAlarm(UserAlarm alarm, List<String> endpoints) {
        userAlarmDao.create(alarm);
        AlarmUtils.scheduleOrCancelAlarm(alarm, alarmManager, getApplicationContext(), getAlarmStopIds(alarm));
        Optional.ofNullable(endpoints).orElse(Collections.emptyList())
                .forEach(endpointName -> {
                    UserEndpoint userEndpoint = new UserEndpoint();
                    userEndpoint.setEndpointName(endpointName);
                    userEndpoint.setAlarm(alarm);
                    userEndpointDao.create(userEndpoint);
                });

        myAlarmsAdapter.addItem(buildAlarmListItem(alarm, TextUtils.join(", ", endpoints)));
    }

    /**
     * Update the given alarm in the database and in the alarms list.
     * @param alarm The given alarm.
     */
    private void updateAlarm(UserAlarm alarm) {
        userAlarmDao.update(alarm);
        AlarmUtils.scheduleOrCancelAlarm(alarm, alarmManager, getApplicationContext(), getAlarmStopIds(alarm));
        List<String> endpoints = userEndpointDao.queryForEq("alarm_id", alarm.getId()).stream()
                .map(UserEndpoint::getEndpointName)
                .collect(Collectors.toList());

        myAlarmsAdapter.updateItem(buildAlarmListItem(alarm, TextUtils.join(", ", endpoints)));
    }
}

package com.jesseoberstein.alert.activities.alarms;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.CreateAlarm;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.adapters.AlarmsAdapter;
import com.jesseoberstein.alert.adapters.EmptyRecyclerViewObserver;
import com.jesseoberstein.alert.data.UserAlarmDao;
import com.jesseoberstein.alert.data.UserEndpointDao;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserEndpoint;
import com.jesseoberstein.alert.models.UserRoute;
import com.jesseoberstein.alert.utils.ActivityUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;

public class ViewAlarms extends AppCompatActivity implements OnDialogClick {
    private AlarmsAdapter myAlarmsAdapter;
    private RuntimeExceptionDao<UserAlarm, Integer> userAlarmDao;
    private RuntimeExceptionDao<UserEndpoint, Integer> userEndpointDao;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userAlarmDao = new UserAlarmDao(getApplicationContext()).getDao();
        this.userEndpointDao = new UserEndpointDao(getApplicationContext()).getDao();

        setContentView(R.layout.activities_view_alarms);

        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle("My Alarms");
            bar.setDisplayHomeAsUpEnabled(true);
        });

//        List<UserAlarm> routeAlarms = userAlarmDao.queryForEq("route_id", "Orange");
//        ArrayList<UserAlarm> userAlarms = routeAlarms.stream()
//                .map(alarm -> {
//                    String endpoints = userEndpointDao.queryForEq("alarm_id", alarm.getId()).stream()
//                            .map(UserEndpoint::getEndpointName)
//                            .collect(Collectors.joining(", "));
//                    return buildAlarmListItem(alarm, endpoints);
//                })
//                .collect(Collectors.toCollection(ArrayList::new));
        UserRoute testRoute = new UserRoute();
        testRoute.setRouteId("Orange");
        testRoute.setRouteName("Orange Line");

        UserAlarm testAlarm = new UserAlarm();
        testAlarm.setActive(true);
        testAlarm.setTime(12, 30);
        testAlarm.setWeekdays(new int[]{0,1,1,1,1,1,0});
        testAlarm.setNickname("Test");
        testAlarm.setRoute(testRoute);
        testAlarm.setStation("North Station");
        testAlarm.setDirection("Northbound");

        UserAlarm testAlarm2 = new UserAlarm();
        testAlarm2.setActive(true);
        testAlarm2.setTime(5, 30);
        testAlarm2.setWeekdays(new int[]{1,0,1,0,1,0,1});
        testAlarm2.setNickname("Test 2");
        testAlarm2.setRoute(testRoute);
        testAlarm2.setStation("Park Street");
        testAlarm2.setDirection("Eastbound");
        testAlarm2.setActive(false);
        ArrayList<UserAlarm> testUserAlarms = new ArrayList<>(Arrays.asList(
                testAlarm2, testAlarm, testAlarm, testAlarm2
        ));

        RecyclerView alarmsView = (RecyclerView) findViewById(R.id.alarm_list);
        myAlarmsAdapter = new AlarmsAdapter(R.layout.list_alarms_new, testUserAlarms);
        alarmsView.setAdapter(myAlarmsAdapter);
        myAlarmsAdapter.registerAdapterDataObserver(new EmptyRecyclerViewObserver(alarmsView, findViewById(R.id.alarm_list_empty)));
        alarmsView.setLayoutManager(new GridLayoutManager(this, 2));

        FloatingActionButton addAlarmView = (FloatingActionButton) findViewById(R.id.add_alarm);
        addAlarmView.setOnClickListener(new StartActivityOnClick(this, EditAlarm.class)
                .withAction(Intent.ACTION_INSERT)
                .withBundle(new Bundle())
                .withRequestCode(CreateAlarm.REQUEST_CODE));

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            UserAlarm alarm = (UserAlarm) data.getExtras().getSerializable(ALARM);

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
        try {
            deleteAlarm(selected.getInt(ALARM_ID));
        } catch (SQLException e) {
            Toast.makeText(this, "Could not delete the selected alarm.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddSelected(Bundle selected) { }

    @Override
    public void onCancelSelected(Bundle selected) { }

//    public RuntimeExceptionDao<UserAlarm, Integer> getUserAlarmDao() {
//        return userAlarmDao;
//    }

    // TODO Pass the stop id instead on alarm creation
//    private String getAlarmStopId(UserAlarm alarm) {
//        return stopsProvider.getStops().stream()
//                .filter(stop -> stop.getName().equals(alarm.getStation()))
//                .map(Stop::getId)
//                .findFirst().orElse("");
//    }

    /**
     * Persist a new alarm and add a new alarm to the alarms list with the given endpoints.
     * @param alarm The given new alarm.
     * @param endpoints A list of the alarm's endpoints.
     */
    private void createAlarm(UserAlarm alarm, List<String> endpoints) {
//        userAlarmDao.create(alarm);
//        Optional.ofNullable(endpoints).orElse(Collections.emptyList())
//                .forEach(endpointName -> {
//                    UserEndpoint userEndpoint = new UserEndpoint();
//                    userEndpoint.setEndpointName(endpointName);
//                    userEndpoint.setAlarm(alarm);
//                    userEndpointDao.create(userEndpoint);
//                });
//
//        AlarmUtils.scheduleOrCancelAlarm(alarm, endpoints, alarmManager, getApplicationContext(), getAlarmStopId(alarm));
//        myAlarmsAdapter.addItem(buildAlarmListItem(alarm, TextUtils.join(", ", endpoints)));
    }

    /** TODO move to CreateAlarm
     * Update the given alarm in the database and in the alarms list.
     * @param alarm The given alarm.
     */
    private void updateAlarm(UserAlarm alarm) {
//        userAlarmDao.update(alarm);
//        List<String> endpoints = userEndpointDao.queryForEq("alarm_id", alarm.getId()).stream()
//                .map(UserEndpoint::getEndpointName)
//                .collect(Collectors.toList());

//        AlarmUtils.scheduleOrCancelAlarm(alarm, endpoints, alarmManager, getApplicationContext(), getAlarmStopId(alarm));
//        myAlarmsAdapter.updateItemById(buildAlarmListItem(alarm, TextUtils.join(", ", endpoints)));
    }

    private void deleteAlarm(int alarmId) throws SQLException {
//        DeleteBuilder<UserEndpoint, Integer> endpointDeleter = userEndpointDao.deleteBuilder();
//        endpointDeleter.where().eq("alarm_id", alarmId);
//        endpointDeleter.delete();
//
//        UserAlarm alarm = userAlarmDao.queryForId(alarmId);
//        AlarmUtils.cancelAlarm(alarm, alarmManager, getApplicationContext());
//        userAlarmDao.deleteById(alarmId);
//        myAlarmsAdapter.removeItemById(alarmId);
    }
}

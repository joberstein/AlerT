package com.jesseoberstein.alert.activities.alarms;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userAlarmDao = new UserAlarmDao(getApplicationContext()).getDao();
        this.userEndpointDao = new UserEndpointDao(getApplicationContext()).getDao();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarms_options, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CreateAlarm.REQUEST_CODE == requestCode)  {
            if (RESULT_OK == resultCode) {
                UserAlarm newAlarm = data.getExtras().getParcelable(ALARM);
                newAlarm.setRoute(new UserRoute(selectedRoute));
                userAlarmDao.create(newAlarm);

                List<String> endpoints = data.getExtras().getStringArrayList(ENDPOINTS);
                Optional.ofNullable(endpoints)
                        .orElse(Collections.emptyList())
                        .forEach(endpointName -> {
                            UserEndpoint userEndpoint = new UserEndpoint();
                            userEndpoint.setEndpointName(endpointName);
                            userEndpoint.setAlarm(newAlarm);
                            userEndpointDao.create(userEndpoint);
                        });

                myAlarmsAdapter.addItem(buildAlarmListItem(newAlarm, TextUtils.join(", ", endpoints)));
            }
        }
    }

    public RuntimeExceptionDao<UserAlarm, Integer> getUserAlarmDao() {
        return userAlarmDao;
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
}

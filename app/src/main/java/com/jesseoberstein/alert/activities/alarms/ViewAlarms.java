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

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.CreateAlarm;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Optional;

import static com.jesseoberstein.alert.listeners.alarm.UpdateAlarmOnClick.NEW_ALARM;
import static com.jesseoberstein.alert.utils.Constants.ALARM_SETTINGS;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.NICKNAME;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;
import static com.jesseoberstein.alert.utils.Constants.STATION;
import static com.jesseoberstein.alert.utils.Constants.STATUS;

public class ViewAlarms extends AppCompatActivity {
    private static String selectedRoute;
    private static int themeColor;
    private CustomListAdapter myAlarmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        myAlarmsAdapter = new CustomListAdapter(this, R.layout.list_alarms, new ArrayList<>());

        ListView listView = (ListView) findViewById(R.id.alarm_list);
        listView.setAdapter(myAlarmsAdapter);
        listView.setEmptyView(findViewById(R.id.alarm_list_empty));
        listView.setOnItemClickListener(new StartActivityOnClick(this, EditAlarm.class)
                .withBundle(selectedBundle)
                .withRequestCode(EditAlarm.REQUEST_CODE));

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
                Bundle newAlarm = data.getExtras().getBundle(NEW_ALARM);
                String nickname = newAlarm.getBundle(ALARM_SETTINGS).getString(NICKNAME);
                String station = newAlarm.getString(STATION);
                String endpoints = TextUtils.join(", ", newAlarm.getStringArrayList(ENDPOINTS));
                boolean status = newAlarm.getBundle(ALARM_SETTINGS).getBoolean(STATUS);
                myAlarmsAdapter.addItem(CustomListItem.buildAlarmListItem(nickname, station, endpoints, status));
            }
        }
    }
}

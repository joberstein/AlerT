package com.jesseoberstein.alert.activities.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.activities.alarm.CreateAlarm;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.jesseoberstein.alert.models.CustomListItem.buildAlarmListItem;
import static com.jesseoberstein.alert.models.CustomListItem.makeDivider;

public class ViewAlarms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_view_alarms);
        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(R.string.orange_line);
            bar.setBackgroundDrawable(getDrawable(R.color.orange_line));
            bar.setDisplayHomeAsUpEnabled(true);
        });

        CustomListAdapter myAlarmsAdapter = new CustomListAdapter(this, R.layout.list_alarms, generateAlarms());
        ListView listView = (ListView) findViewById(R.id.alarm_list);
        listView.setOnItemClickListener(new StartActivityOnClick(this, EditAlarm.class));
        listView.setAdapter(myAlarmsAdapter);

        Bundle bundle = new Bundle();
        // Pass in routes to filter (user already has them in their routes list)
        bundle.putString("route", "Orange Line");

        FloatingActionButton addAlarmView = (FloatingActionButton) findViewById(R.id.add_alarm);
        addAlarmView.setOnClickListener(
                new StartActivityOnClick(this, CreateAlarm.class)
                        .withAction(Intent.ACTION_INSERT)
                        .withBundle(bundle));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarms_options, menu);
        return true;
    }

    // Static test alarm data.
    private ArrayList<CustomListItem> generateAlarms(){
        return new ArrayList<>(Arrays.asList(
                buildAlarmListItem("Work", "Malden Center", "Forest Hills", true),
                buildAlarmListItem("Home", "North Station", "Oak Grove", true),
                makeDivider(),
                buildAlarmListItem("Alarm 1", "North Station", "Forest Hills", false),
                buildAlarmListItem("Alarm 2", "Ruggles", "Oak Grove", false)
        ));
    }
}

package com.jesseoberstein.alert.activities.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.jesseoberstein.alert.models.CustomListItem.buildAlarmListItem;
import static com.jesseoberstein.alert.models.CustomListItem.makeDivider;

public class Alarms extends AppCompatActivity {
    private CustomListAdapter myAlarmsAdapter;

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

        myAlarmsAdapter = new CustomListAdapter(this, R.layout.list_alarms, generateAlarms());
        ListView listView = (ListView) findViewById(R.id.alarm_list);
        listView.setAdapter(myAlarmsAdapter);
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

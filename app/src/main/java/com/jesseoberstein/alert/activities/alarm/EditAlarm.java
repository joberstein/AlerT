package com.jesseoberstein.alert.activities.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDaysDialog;
import com.jesseoberstein.alert.interfaces.AlarmDaySetter;
import com.jesseoberstein.alert.interfaces.AlarmDurationSetter;
import com.jesseoberstein.alert.interfaces.AlarmRepeatSetter;
import com.jesseoberstein.alert.interfaces.AlarmRouteSetter;
import com.jesseoberstein.alert.interfaces.AlarmStopSetter;
import com.jesseoberstein.alert.interfaces.AlarmTimeSetter;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.utils.ActivityUtils.setIconColor;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.CURRENT_TAB;
import static com.jesseoberstein.alert.utils.Constants.DRAFT_ALARM;

public class EditAlarm
        extends AppCompatActivity
        implements OnDialogClick, AlarmTimeSetter, AlarmRepeatSetter, AlarmDaySetter,
                   AlarmDurationSetter, AlarmRouteSetter, AlarmStopSetter {

    public static final int REQUEST_CODE = 3;
    private ViewPager pager;
    private AlarmPagerAdapter alarmPagerAdapter;
    private ArrayList<String> endpoints;
    private UserAlarm alarm = new UserAlarm(); // TODO for testing only
    private UserAlarm draftAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If the activity has been restarted (e.g. to apply a new theme), restore the current
        // instance of the draft alarm if it exists.  Otherwise, clone the existing alarm.
        UserAlarm currentDraftAlarm = (UserAlarm) getIntent().getSerializableExtra(DRAFT_ALARM);
        this.draftAlarm = Optional.ofNullable(currentDraftAlarm).orElse(new UserAlarm(alarm));

        if (savedInstanceState != null) {
            this.alarm = (UserAlarm) savedInstanceState.getSerializable(ALARM);
            this.draftAlarm = (UserAlarm) savedInstanceState.getSerializable(DRAFT_ALARM);
        }

        setTheme(AlertUtils.getTheme(this.draftAlarm.getRoute()));
        setContentView(R.layout.activities_edit_alarm_new);

        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle("Edit Alarm");
            bar.setDisplayHomeAsUpEnabled(true);
        });

        alarmPagerAdapter = new AlarmPagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.alarm_settings_pager);
        pager.setAdapter(alarmPagerAdapter);
        pager.setCurrentItem(getIntent().getIntExtra(CURRENT_TAB, 0));

        TabLayout settingsTabs = (TabLayout) findViewById(R.id.alarm_settings_tabs);
        settingsTabs.setupWithViewPager(pager, true);
        setTabIcons(settingsTabs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarms_save, menu);
        setIconColor(this, menu.getItem(0).getIcon(), R.attr.menuItemIconColor);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(DRAFT_ALARM, getDraftAlarm());
        savedInstanceState.putSerializable(ALARM, getAlarm());
        super.onSaveInstanceState(savedInstanceState);
    }

    public AlarmPagerAdapter getAlarmPagerAdapter() {
        return alarmPagerAdapter;
    }

    public ArrayList<String> getEndpoints() {
        return endpoints;
    }

    public UserAlarm getAlarm() {
        return alarm;
    }

    /**
     * Get the cloned alarm to modify. If the user decides not to save the alarm, the original
     * instance is preserved.
     */
    public UserAlarm getDraftAlarm() {
        return draftAlarm;
    }

    private void setTabIcons(TabLayout tabs) {
        int[] icons = {R.drawable.ic_alarm, R.drawable.ic_train};
        IntStream.range(0, icons.length).forEach(i ->
            Optional.ofNullable(tabs.getTabAt(i)).ifPresent(tab -> {
                tab.setIcon(icons[i]);
                tab.setContentDescription("tab " + Integer.toString(icons[i]));
            })
        );
    }

    @Override
    public void onAddSelected(Bundle alarm) {
        Intent intent = new Intent();
        intent.putExtras(alarm);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRemoveSelected(Bundle alarm) {}

    @Override
    public void onCancelSelected(Bundle alarm) {}

    public void onAlarmTimeSet(int hour, int minute) {
        draftAlarm.setTime(hour, minute);
    }

    @Override
    public void onAlarmRepeatSet(RepeatType repeatType) {
        this.draftAlarm.setRepeatType(repeatType);

        if (RepeatType.CUSTOM.equals(repeatType)) {
            new SetDaysDialog().show(getSupportFragmentManager(), "setDays");
        }
    }

    @Override
    public void onAlarmDaysSet(int[] days) {
        this.draftAlarm.setSelectedDays(days);
    }

    @Override
    public void onAlarmDurationSet(long duration) {
        this.draftAlarm.setDuration(duration);
    }

    @Override
    public void onAlarmRouteSet(Route route) {
        this.draftAlarm.setRoute(route);
        // Once the route is set, restart the activity to apply the selected route'
        Intent intent = getIntent();
        intent.putExtra(DRAFT_ALARM, this.draftAlarm);
        intent.putExtra(CURRENT_TAB, pager.getCurrentItem());
        startActivity(intent);
        finish();
    }

    @Override
    public void onAlarmStopSet(Stop stop) {
        this.draftAlarm.setStop(stop);
    }
}
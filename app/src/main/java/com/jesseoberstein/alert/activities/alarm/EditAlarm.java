package com.jesseoberstein.alert.activities.alarm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.jesseoberstein.alert.interfaces.AlarmRepeatSetter;
import com.jesseoberstein.alert.interfaces.AlarmTimeSetter;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.utils.ActivityUtils.getAttrValue;
import static com.jesseoberstein.alert.utils.ActivityUtils.setIconColor;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.DRAFT_ALARM;

public class EditAlarm
        extends AppCompatActivity
        implements OnDialogClick, AlarmTimeSetter, AlarmRepeatSetter, AlarmDaySetter {

    public static final int REQUEST_CODE = 3;
    private AlarmPagerAdapter alarmPagerAdapter;
    private ArrayList<String> endpoints;
    private UserAlarm alarm = new UserAlarm(); // TODO for testing only
    private UserAlarm draftAlarm = new UserAlarm(alarm);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.alarm = (UserAlarm) savedInstanceState.getSerializable(ALARM);
            this.draftAlarm = (UserAlarm) savedInstanceState.getSerializable(DRAFT_ALARM);
        }

        setTheme(R.style.AlarmSettingsDark_Orange);
        setContentView(R.layout.activities_edit_alarm_new);

        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle("Edit Alarm");
            bar.setDisplayHomeAsUpEnabled(true);
        });

        alarmPagerAdapter = new AlarmPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.alarm_pager);
        pager.setAdapter(alarmPagerAdapter);
        pager.setCurrentItem(0);

        TabLayout settingsTabs = (TabLayout) findViewById(R.id.alarm_settings);
        settingsTabs.setupWithViewPager(pager, true);
        styleSettingsTabs(settingsTabs);
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

    private void styleSettingsTabs(TabLayout tabs) {
        // Set selected tab indicator color.
        tabs.setBackgroundColor(getAttrValue(this, R.attr.tabsBackgroundColor));
        tabs.setSelectedTabIndicatorColor(getAttrValue(this, R.attr.tabSelectedIndicatorColor));

        // Set each tab's icon.
        int numTabs = tabs.getTabCount();
        int[] icons = new int[numTabs];
        Arrays.fill(icons, 0);
        icons[0] = R.drawable.ic_alarm;
        icons[1] = R.drawable.ic_train;

        IntStream.range(0, numTabs)
            .filter(i -> icons[i] != 0)
            .forEach(i -> {
                Drawable icon = getDrawable(icons[i]);
                setIconColor(this, icon, R.attr.tabIconColor);
                tabs.getTabAt(i).setIcon(icon);
            });
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
    public void onAlarmRepeatSet(RepeatType selected, RepeatType previous) {
        this.draftAlarm.setRepeatType(selected);

        // If switching to custom from a different repeat type, reset all of the selected days.
        if (!selected.equals(previous)) {
            this.draftAlarm.setWeekdays(RepeatType.NEVER.getSelectedDays());
        }

        if (RepeatType.CUSTOM.equals(selected)) {
            new SetDaysDialog().show(getSupportFragmentManager(), "setDays");
        }
    }

    @Override
    public void onAlarmDaysSet(int[] days) {
        this.draftAlarm.setWeekdays(days);
    }
}
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
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.utils.ActivityUtils.getAttrValue;
import static com.jesseoberstein.alert.utils.ActivityUtils.setIconColor;

public class EditAlarm extends AppCompatActivity implements OnDialogClick {
    public static final int REQUEST_CODE = 3;
    private AlarmPagerAdapter alarmPagerAdapter;
    private ArrayList<String> endpoints;
    private UserAlarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public AlarmPagerAdapter getAlarmPagerAdapter() {
        return alarmPagerAdapter;
    }

    public ArrayList<String> getEndpoints() {
        return endpoints;
    }

    public UserAlarm getAlarm() {
        return alarm;
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
}
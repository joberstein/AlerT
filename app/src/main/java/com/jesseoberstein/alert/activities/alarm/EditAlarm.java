package com.jesseoberstein.alert.activities.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.listeners.alarm.ChangeNavOnPageSelected;
import com.jesseoberstein.alert.listeners.alarm.DecrementViewOnClick;
import com.jesseoberstein.alert.listeners.alarm.IncrementViewOnClick;
import com.jesseoberstein.alert.listeners.alarm.UpdateAlarmOnClick;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.ArrayList;
import java.util.Optional;

import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.THEME;

public class EditAlarm extends AppCompatActivity implements OnDialogClick {
    public static final int REQUEST_CODE = 3;
    private static int themeColor;
    private AlarmPagerAdapter alarmPagerAdapter;
    private ArrayList<String> endpoints;
    private UserAlarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle selectedBundle = getIntent().getExtras();
        themeColor = selectedBundle.getInt(COLOR);
        setTheme(selectedBundle.getInt(THEME));
        endpoints = selectedBundle.getStringArrayList(ENDPOINTS);
        alarm = (UserAlarm) selectedBundle.getSerializable(ALARM);

        setContentView(R.layout.activities_edit_alarm);
        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(alarm.getStation());
            bar.setSubtitle(TextUtils.join(", ", endpoints));
            bar.setBackgroundDrawable(getDrawable(themeColor));
            bar.setDisplayHomeAsUpEnabled(true);
        });

        View previous = findViewById(R.id.previous_step);
        View next = findViewById(R.id.next_step);
        View submit = findViewById(R.id.submit_alarm);
        alarmPagerAdapter = new AlarmPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.alarm_pager);
        pager.setAdapter(alarmPagerAdapter);
        pager.addOnPageChangeListener(new ChangeNavOnPageSelected(previous, next, submit));
        pager.setCurrentItem(0);

        next.setOnClickListener(new IncrementViewOnClick(pager));
        previous.setOnClickListener(new DecrementViewOnClick(pager));
        findViewById(R.id.submit_alarm).setOnClickListener(new UpdateAlarmOnClick(this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.stepper_dots);
        tabLayout.setBackgroundColor(getColor(themeColor));
        tabLayout.setupWithViewPager(pager, true);
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
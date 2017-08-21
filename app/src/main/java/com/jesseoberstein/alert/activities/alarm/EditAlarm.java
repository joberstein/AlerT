package com.jesseoberstein.alert.activities.alarm;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.listeners.alarm.ChangeNavOnPageSelected;
import com.jesseoberstein.alert.listeners.alarm.DecrementViewOnClick;
import com.jesseoberstein.alert.listeners.alarm.IncrementViewOnClick;

import java.util.ArrayList;
import java.util.Optional;

import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.STATION;
import static com.jesseoberstein.alert.utils.Constants.THEME;

public class EditAlarm extends AppCompatActivity {
    private static int themeColor;
    private FragmentPagerAdapter adapter;
    private String station;
    private ArrayList<String> endpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle selectedBundle = getIntent().getExtras();
        themeColor = selectedBundle.getInt(COLOR);
        setTheme(selectedBundle.getInt(THEME));
        station = selectedBundle.getString(STATION);
        endpoints = selectedBundle.getStringArrayList(ENDPOINTS);

        setContentView(R.layout.activities_edit_alarm);
        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(station);
            bar.setSubtitle(TextUtils.join(", ", endpoints));
            bar.setBackgroundDrawable(getDrawable(themeColor));
            bar.setDisplayHomeAsUpEnabled(true);
        });

        View previous = findViewById(R.id.previous_step);
        View next = findViewById(R.id.next_step);
        View submit = findViewById(R.id.submit_alarm);
        adapter = new AlarmPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.alarm_pager);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ChangeNavOnPageSelected(previous, next, submit));
        pager.setCurrentItem(0);

        next.setOnClickListener(new IncrementViewOnClick(pager));
        previous.setOnClickListener(new DecrementViewOnClick(pager));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.stepper_dots);
        tabLayout.setBackgroundColor(getColor(themeColor));
        tabLayout.setupWithViewPager(pager, true);
    }
}
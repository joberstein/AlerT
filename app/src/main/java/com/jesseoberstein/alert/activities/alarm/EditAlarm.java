package com.jesseoberstein.alert.activities.alarm;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jesseoberstein.alert.listeners.alarm.ChangeNavOnPageSelected;
import com.jesseoberstein.alert.listeners.alarm.DecrementViewOnClick;
import com.jesseoberstein.alert.listeners.alarm.IncrementViewOnClick;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;

import java.util.Optional;

public class EditAlarm extends AppCompatActivity {
    private static final int THEME_COLOR = R.color.orange_line;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_edit_alarm);
        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle("North Station");
            bar.setSubtitle("Forest Hills");
            bar.setBackgroundDrawable(getDrawable(THEME_COLOR));
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
        tabLayout.setupWithViewPager(pager, true);
    }
}
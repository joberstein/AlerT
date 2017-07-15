package com.jesseoberstein.alert.activities.alarm;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.StepBackOnClick;
import com.jesseoberstein.alert.StepNextOnClick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EditAlarm extends AppCompatActivity {
    private static final int THEME_COLOR = R.color.orange_line;

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

        View page = findViewById(R.id.page);
        View stepper = findViewById(R.id.stepper);
        TextView stepText = (TextView) findViewById(R.id.stepText);
        ImageView time = (ImageView) findViewById(R.id.time);
        ImageView day = (ImageView) findViewById(R.id.day);
        ImageView settings = (ImageView) findViewById(R.id.settings);
        List<ImageView> steps = new ArrayList<>(Arrays.asList(time, day, settings));
        View previous = findViewById(R.id.previous_step);
        View next = findViewById(R.id.next_step);

        stepper.setBackgroundColor(getColor(THEME_COLOR));
        previous.setOnClickListener(new StepBackOnClick(page, stepText, previous, next, steps));
        next.setOnClickListener(new StepNextOnClick(page, stepText, previous, next, steps));
    }
}
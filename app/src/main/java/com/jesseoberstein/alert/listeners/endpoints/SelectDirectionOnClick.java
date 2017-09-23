package com.jesseoberstein.alert.listeners.endpoints;

import android.content.Context;
import android.view.View;

import com.jesseoberstein.alert.activities.alarm.CreateAlarm;

import java.util.Optional;

import static android.support.design.widget.TabLayout.*;

public class SelectDirectionOnClick implements OnTabSelectedListener {
    private Context context;

    public SelectDirectionOnClick(Context context) {
        this.context = context;

    }

    @Override
    public void onTabSelected(Tab tab) {
        String className = context.getClass().getSimpleName();
        String tabText = Optional.ofNullable(tab.getText())
                .map(CharSequence::toString).orElse("");

        switch (className) {
            case "CreateAlarm":
                CreateAlarm createAlarm = (CreateAlarm) context;
                createAlarm.getLinesView().setVisibility(View.GONE);
                createAlarm.getEndpointsView().setVisibility(View.GONE);
                createAlarm.setUpLineAndEndpointButtons(tabText);
        }
    }

    @Override
    public void onTabUnselected(Tab tab) {}

    @Override
    public void onTabReselected(Tab tab) {}
}

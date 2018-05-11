package com.jesseoberstein.alert.listeners;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.jesseoberstein.alert.activities.alarm.CreateAlarm;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static android.view.View.OnClickListener;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;

public class StartActivityOnClick implements OnClickListener, OnItemClickListener {
    private Activity origin;
    private String originClassName;
    private Class<?> destination;
    private Bundle extras;
    private String action;
    private int requestCode;

    public StartActivityOnClick(Activity origin, Class<?> destination) {
        this.origin = origin;
        this.originClassName = origin.getClass().getSimpleName();
        this.destination = destination;
        this.requestCode = -1;
    }

    public StartActivityOnClick withAction(String action) {
        this.action = action;
        return this;
    }

    public StartActivityOnClick withBundle(Bundle extras) {
        this.extras = extras;
        return this;
    }

    public StartActivityOnClick withRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public void onClick(View view) {
        this.extras = Optional.ofNullable(this.extras).orElse(new Bundle());

        switch (this.originClassName) {
            case "CreateAlarm":
                editCreatedAlarm();
                break;
        }

        this.startActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.extras = Optional.ofNullable(this.extras).orElse(new Bundle());
        CustomListItem selected = (CustomListItem) parent.getItemAtPosition(position);

        switch (this.originClassName) {
            case "ViewRoutes":
                forwardToSelectedRoute(selected);
                break;
            case "ViewAlarms":
                forwardToSelectedAlarm(selected);
                break;
        }

        this.startActivity();
    }

    private void startActivity() {
        Intent intent = new Intent(this.origin, this.destination);
        Optional.ofNullable(this.extras).ifPresent(intent::putExtras);
        Optional.ofNullable(this.action).ifPresent(intent::setAction);
        this.origin.startActivityForResult(intent, this.requestCode);
    }

    private void editCreatedAlarm() {
        CreateAlarm createAlarm = (CreateAlarm) this.origin;

        UserAlarm newAlarm = new UserAlarm();
        newAlarm.setNickname(createAlarm.getNicknameText().getText().toString());
        newAlarm.setStation(createAlarm.getStationAutoComplete().getText().toString());

        TabLayout tabs = createAlarm.getDirectionTabs();
//        newAlarm.setDirection(tabs.getTabAt(tabs.getSelectedTabPosition()).getText().toString());

        // Defaults
        newAlarm.setActive(true);
        newAlarm.setDuration(1);

        this.extras.putSerializable(ALARM, newAlarm);

        GridView endpoints = createAlarm.getEndpointButtons();
        ArrayList<String> selectedEndpoints = IntStream.range(0, endpoints.getChildCount())
                .mapToObj(i -> (ToggleButton) endpoints.getChildAt(i))
                .filter(CompoundButton::isChecked)
                .map(btn -> btn.getText().toString())
                .collect(Collectors.toCollection(ArrayList::new));

        this.extras.putStringArrayList(ENDPOINTS, selectedEndpoints);
    }

    private void forwardToSelectedRoute(CustomListItem item) {
//        UserRoute userRoute = new UserRoute(item.getPrimaryText());
//        userRoute.setRouteResources();
//        this.extras.putString(ROUTE, userRoute.getRouteName());
//        this.extras.putInt(COLOR, userRoute.getColor());
//        this.extras.putInt(THEME, userRoute.getTheme());
    }

    private void forwardToSelectedAlarm(CustomListItem item) {
//        UserAlarm userAlarm = ((ViewAlarms) this.origin).getUserAlarmDao().queryForId((int) item.getId());
//        this.extras.putSerializable(ALARM, userAlarm);
//        this.extras.putStringArrayList(ENDPOINTS,
//                Arrays.stream(item.getTertiaryText().split(","))
//                        .collect(Collectors.toCollection(ArrayList::new)));
    }
}

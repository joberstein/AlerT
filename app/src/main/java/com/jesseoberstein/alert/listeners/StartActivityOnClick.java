package com.jesseoberstein.alert.listeners;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarms.ViewAlarms;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserRoute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static android.view.View.OnClickListener;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.IS_UPDATE;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;
import static com.jesseoberstein.alert.utils.Constants.THEME;

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
        UserAlarm newAlarm = new UserAlarm();

        EditText nickname = (EditText) this.origin.findViewById(R.id.nickname);
        newAlarm.setNickname(nickname.getText().toString());

        AutoCompleteTextView station = (AutoCompleteTextView) this.origin.findViewById(R.id.station);
        newAlarm.setStation(station.getText().toString());

        // Defaults
        newAlarm.setActive(true);
        newAlarm.setDuration(1);

        // Search through this mess of a layout to get all of the toggled enpoints.
        // Structure: LinearLayout (root) -> LinearLayout (Button row) -> ToggleButton
        LinearLayout endpoints = (LinearLayout) this.origin.findViewById(R.id.directions);
        ArrayList<String> selectedEndpoints = IntStream.range(0, endpoints.getChildCount())
                .mapToObj(rowIdx -> ((LinearLayout) endpoints.getChildAt(rowIdx)))
                .flatMap(row -> IntStream.range(0, row.getChildCount())
                        .mapToObj(btnIdx -> ((ToggleButton) row.getChildAt(btnIdx)))
                        .collect(Collectors.toList()).stream())
                .filter(CompoundButton::isChecked)
                .map(btn -> btn.getText().toString())
                .collect(Collectors.toCollection(ArrayList::new));

        this.extras.putStringArrayList(ENDPOINTS, selectedEndpoints);
        this.extras.putSerializable(ALARM, newAlarm);
    }

    private void forwardToSelectedRoute(CustomListItem item) {
        UserRoute userRoute = new UserRoute(item.getPrimaryText());
        userRoute.setRouteResources();
        this.extras.putString(ROUTE, userRoute.getRouteName());
        this.extras.putInt(COLOR, userRoute.getColor());
        this.extras.putInt(THEME, userRoute.getTheme());
    }

    private void forwardToSelectedAlarm(CustomListItem item) {
        UserAlarm userAlarm = ((ViewAlarms) this.origin).getUserAlarmDao().queryForId(item.getId());
        this.extras.putSerializable(ALARM, userAlarm);
        this.extras.putStringArrayList(ENDPOINTS,
                Arrays.stream(item.getTertiaryText().split(","))
                        .collect(Collectors.toCollection(ArrayList::new)));
    }
}

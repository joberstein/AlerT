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
import com.jesseoberstein.alert.interfaces.AlarmDirectionSetter;
import com.jesseoberstein.alert.interfaces.AlarmDurationSetter;
import com.jesseoberstein.alert.interfaces.AlarmEndpointSetter;
import com.jesseoberstein.alert.interfaces.AlarmRepeatSetter;
import com.jesseoberstein.alert.interfaces.AlarmRouteSetter;
import com.jesseoberstein.alert.interfaces.AlarmStopSetter;
import com.jesseoberstein.alert.interfaces.AlarmTimeSetter;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.interfaces.data.EndpointsReceiver;
import com.jesseoberstein.alert.interfaces.data.RoutesReceiver;
import com.jesseoberstein.alert.interfaces.data.StopsReceiver;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.tasks.QueryDirectionsTask;
import com.jesseoberstein.alert.tasks.QueryEndpointsTask;
import com.jesseoberstein.alert.tasks.QueryRoutesTask;
import com.jesseoberstein.alert.tasks.QueryStopsTask;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.models.mbta.Endpoint.PSUEDO_ENDPOINT_NAMES;
import static com.jesseoberstein.alert.utils.ActivityUtils.setIconColor;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.CURRENT_TAB;
import static com.jesseoberstein.alert.utils.Constants.DRAFT_ALARM;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class EditAlarm extends AppCompatActivity implements OnDialogClick,
        AlarmTimeSetter, AlarmRepeatSetter, AlarmDaySetter, AlarmDurationSetter,
        AlarmRouteSetter, AlarmStopSetter, AlarmDirectionSetter, AlarmEndpointSetter,
        RoutesReceiver, StopsReceiver, DirectionsReceiver, EndpointsReceiver {

    public static final int REQUEST_CODE = 3;
    private ViewPager pager;
    private AlarmPagerAdapter alarmPagerAdapter;
    private UserAlarm alarm = new UserAlarm(); // TODO for testing only
    private UserAlarm draftAlarm;
    private List<Route> routeList;
    private List<Stop> stopList;
    private List<Direction> directionList;
    private List<Endpoint> endpointList;

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

        // Query for routes.
        new QueryRoutesTask(this).execute();
        String routeId = this.draftAlarm.getRouteId();

        // Query for directions if the activity has been restarted (as the result of selecting a new route).
        if (!isNull(routeId)) {
            new QueryDirectionsTask(this).execute(routeId);
        }

        // Set the theme, content view, and action bar.
        setTheme(AlertUtils.getTheme(this.draftAlarm.getRoute()));
        setContentView(R.layout.activities_edit_alarm_new);

        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle("Edit Alarm");
            bar.setDisplayHomeAsUpEnabled(true);
        });

        // Set up the tab layout view pager.
        alarmPagerAdapter = new AlarmPagerAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.alarm_settings_pager);
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

    public List<Route> getRouteList() {
        return routeList;
    }

    public List<Direction> getDirectionList() {
        return directionList;
    }

    public List<Stop> getStopList() {
        return stopList;
    }

    public List<Endpoint> getEndpointList() {
        return endpointList;
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
        this.draftAlarm.setDirection(null);
        this.draftAlarm.setStop(null);
        this.draftAlarm.setEndpoints(new ArrayList<>());

        // Once the route is set, restart the activity to apply the selected route'
        Intent intent = getIntent();
        intent.putExtra(DRAFT_ALARM, this.draftAlarm);
        intent.putExtra(CURRENT_TAB, pager.getCurrentItem());
        startActivity(intent);
        finish();
    }

    @Override
    public void onAlarmDirectionSet(Direction direction) {
        this.draftAlarm.setDirection(direction);

        // Once the direction is set, query for endpoints.
        String routeId = this.draftAlarm.getRouteId();
        String directionId = Integer.toString(direction.getId());
        new QueryEndpointsTask(this).execute(routeId, directionId);
    }

    @Override
    public void onAlarmStopSet(Stop stop) {
        this.draftAlarm.setStop(stop);
    }

    @Override
    public void onAlarmEndpointsSet(List<Endpoint> endpoints) {
        this.draftAlarm.setEndpoints(endpoints);
    }

    @Override
    public void onReceiveRoutes(List<Route> routes) {
        this.routeList = Collections.unmodifiableList(new ArrayList<>(routes));

    }

    @Override
    public void onReceiveDirections(List<Direction> directions) {
        this.directionList = Collections.unmodifiableList(new ArrayList<>(directions));
    }

    @Override
    public void onReceiveEndpoints(List<Endpoint> endpoints) {
        this.endpointList = Collections.unmodifiableList(new ArrayList<>(endpoints)).stream()
            .filter(endpoint -> !PSUEDO_ENDPOINT_NAMES.contains(endpoint.getName()))
            .collect(toList());

        this.draftAlarm.setEndpoints(this.endpointList);

        // Once endpoints are received, query for stops.
        String routeId = this.draftAlarm.getRouteId();
        new QueryStopsTask(this).execute(routeId);
    }

    @Override
    public void onReceiveStops(List<Stop> stops) {
        // Before setting the stop list, filter out any endpoints.
        List<String> endpointNames = this.endpointList.stream().map(Endpoint::getName).collect(toList());
        List<Stop> stopsWithoutEndpoints = stops.stream()
                .filter(stop -> !endpointNames.contains(stop.getName()))
                .collect(toList());

        this.stopList = Collections.unmodifiableList(stopsWithoutEndpoints);

        // If the stop list doesn't contain the selected stop, null out the alarm's stop.
        if (!this.stopList.contains(this.draftAlarm.getStop())) {
            this.draftAlarm.setStop(null);
        }
    }
}
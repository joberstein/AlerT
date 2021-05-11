package com.jesseoberstein.alert.activities.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.base.BaseActivity;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.data.database.AppDatabase;
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
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.interfaces.data.EndpointsReceiver;
import com.jesseoberstein.alert.interfaces.data.RoutesReceiver;
import com.jesseoberstein.alert.interfaces.data.StopsReceiver;
import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.tasks.DeleteAlarmEndpointsTask;
import com.jesseoberstein.alert.tasks.InsertAlarmEndpointsTask;
import com.jesseoberstein.alert.tasks.InsertAlarmTask;
import com.jesseoberstein.alert.tasks.QueryDirectionsTask;
import com.jesseoberstein.alert.tasks.QueryEndpointsTask;
import com.jesseoberstein.alert.tasks.QueryRoutesTask;
import com.jesseoberstein.alert.tasks.QueryStopsTask;
import com.jesseoberstein.alert.tasks.UpdateAlarmTask;
import com.jesseoberstein.alert.utils.AlarmManagerHelper;
import com.jesseoberstein.alert.utils.AlarmUtils;
import com.jesseoberstein.alert.utils.AlertUtils;
import com.jesseoberstein.alert.utils.Constants;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.inject.Inject;

import static com.jesseoberstein.alert.models.mbta.Endpoint.PSUEDO_ENDPOINT_NAMES;
import static com.jesseoberstein.alert.utils.ActivityUtils.setDrawableColor;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.CURRENT_TAB;
import static com.jesseoberstein.alert.utils.Constants.DRAFT_ALARM;
import static java.util.stream.Collectors.toList;

public class EditAlarm extends BaseActivity implements OnDialogClick,
        AlarmTimeSetter, AlarmRepeatSetter, AlarmDaySetter, AlarmDurationSetter,
        AlarmRouteSetter, AlarmStopSetter, AlarmDirectionSetter, AlarmEndpointSetter,
        RoutesReceiver, StopsReceiver, DirectionsReceiver, EndpointsReceiver, AlarmReceiver {

    @Inject
    ActionBar actionBar;

    @Inject
    FragmentManager fragmentManager;

    @Inject
    UserAlarmScheduler userAlarmScheduler;

    @Inject
    AlarmManagerHelper alarmManagerHelper;

    @Inject
    AppDatabase database;

    private ViewPager pager;
    private UserAlarmWithRelations alarm;
    private UserAlarmWithRelations draftAlarm;
    private List<Route> routeList;
    private List<Stop> stopList;
    private List<Direction> directionList;
    private List<Endpoint> endpointList;
    private Snackbar validationSnackbar;
    private boolean isAlarmUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getIntent().getIntExtra(COLOR, R.style.AlarmSettingsDark_Default));
        super.onCreate(savedInstanceState);

        this.alarm = (UserAlarmWithRelations) getIntent().getSerializableExtra(ALARM);
        this.draftAlarm = (UserAlarmWithRelations) getIntent().getSerializableExtra(DRAFT_ALARM);

        if (savedInstanceState != null) {
            this.draftAlarm = (UserAlarmWithRelations) savedInstanceState.getSerializable(DRAFT_ALARM);
        }

        this.isAlarmUpdate = Optional.ofNullable(this.alarm).isPresent();
        boolean isActivityRestarted = Optional.ofNullable(this.draftAlarm).isPresent();

        if (this.isAlarmUpdate && !isActivityRestarted) {
            this.draftAlarm = new UserAlarmWithRelations(this.alarm);
            Optional.of(this.alarm.getDirection()).ifPresent(this::onAlarmDirectionSet);
            Optional.of(this.alarm.getStop()).ifPresent(this::onAlarmStopSet);
            this.onAlarmEndpointsSet(this.alarm.getEndpoints());
        } else if (this.draftAlarm == null) {
            this.draftAlarm = new UserAlarmWithRelations();
            userAlarmScheduler.setDefaultAlarmTime(this.draftAlarm.getAlarm());
        }

        setUpView();
        userAlarmScheduler.setNextFiringDayString(this.draftAlarm.getAlarm());

        // Query for routes.
        new QueryRoutesTask(this, database).execute();

        // Query for directions if the activity was restarted with a route change.
        Optional.ofNullable(this.draftAlarm)
            .map(UserAlarmWithRelations::getAlarm)
            .map(UserAlarm::getRouteId)
            .ifPresent(routeId -> new QueryDirectionsTask(this, database).execute(routeId));
    }

    private void setUpView() {
        setContentView(R.layout.activities_edit_alarm);

        Optional.ofNullable(this.actionBar).ifPresent(bar -> {
            bar.setTitle(this.isAlarmUpdate ? R.string.edit_alarm_page : R.string.new_alarm_page);
            bar.setDisplayHomeAsUpEnabled(true);
        });

        // Set up the tab layout view pager.
        AlarmPagerAdapter alarmPagerAdapter = new AlarmPagerAdapter(this.fragmentManager);
        pager = findViewById(R.id.alarm_settings_pager);
        pager.setAdapter(alarmPagerAdapter);
        pager.setCurrentItem(getIntent().getIntExtra(CURRENT_TAB, 0));

        TabLayout settingsTabs = findViewById(R.id.alarm_settings_tabs);
        settingsTabs.setupWithViewPager(pager, true);
        setTabIcons(settingsTabs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarms_save, menu);

        MenuItem save = menu.getItem(0);
        save.setContentDescription("Save Alarm");
        setDrawableColor(this, save.getIcon(), R.attr.menuItemIconColor);
        save.setOnMenuItemClickListener(this::saveAlarm);

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(DRAFT_ALARM, getDraftAlarmWithRelations());
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Get the cloned alarm to modify. If the user decides not to save the alarm, the original
     * instance is preserved.
     */
    public UserAlarm getDraftAlarm() {
        return draftAlarm.getAlarm();
    }

    public UserAlarmWithRelations getDraftAlarmWithRelations() {
        return draftAlarm;
    }

    public Snackbar getValidationSnackbar() {
        return validationSnackbar;
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

    /**
     * If the draft alarm is valid, save it to the database.  If not, show the validation snackbar.
     * @param item dummy arg
     * @return Always returns false so that this method doesn't consume the click.
     */
    private boolean saveAlarm(MenuItem item) {
        if (this.draftAlarm.isValid()) {
            if (this.isAlarmUpdate) {
                new UpdateAlarmTask(this, database).execute(this.draftAlarm);
            } else {
                new InsertAlarmTask(this, database).execute(this.draftAlarm.getAlarm());
            }
        } else {
            this.validationSnackbar = createValidationSnackbar();
            this.validationSnackbar.show();
        }
        return false;
    }

    /**
     * Create a snackbar to show for an alarm validation error.
     * @return A snackbar containing the validation error and an action button for fixing it.
     */
    private Snackbar createValidationSnackbar() {
        Snackbar snackbar = Snackbar.make(pager, getValidationErrorMessage(), Snackbar.LENGTH_INDEFINITE);

        if (getSectionIdToFix() > -1) {
            snackbar.setAction(R.string.fix, view -> findViewById(getSectionIdToFix()).performClick());
            snackbar.setActionTextColor(getResources().getColor(R.color.alert_red, null));
        }

        TextView snackbarTextView = snackbar.getView().findViewById(R.id.snackbar_text);
        snackbarTextView.setTextColor(getResources().getColor(R.color.white, null));
        return snackbar;
    }

    /**
     * Get the id of the section that has failed validation and needs to be fixed.
     * @return An id of a section to click on.
     */
    private int getSectionIdToFix() {
        switch (this.draftAlarm.getErrors().get(0)) {
            case Constants.CUSTOM_REPEAT_TYPE:
                return R.id.alarmSettings_repeat;
            case Constants.ROUTE:
                return R.id.alarmSettings_route;
            case Constants.DIRECTION_ID:
                return R.id.alarmSettings_direction;
            case Constants.STOP_ID:
                return R.id.alarmSettings_stop;
            case Constants.ENDPOINTS:
                return R.id.alarmSettings_endpoints;
        }
        return -1;
    }

    /**
     * Get the error message shown for the section that has failed validation.
     * @return An error message based on an invalid alarm property.
     */
    private int getValidationErrorMessage() {
        switch (this.draftAlarm.getErrors().get(0)) {
            case Constants.CUSTOM_REPEAT_TYPE:
                return R.string.repeat_custom_invalid;
            case Constants.ROUTE:
                return R.string.route_invalid;
            case Constants.DIRECTION_ID:
                return R.string.direction_invalid;
            case Constants.STOP_ID:
                return R.string.stop_invalid;
            case Constants.ENDPOINTS:
                return R.string.endpoints_invalid;
        }
        return -1;
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
        userAlarmScheduler.setAlarmTime(this.draftAlarm.getAlarm(), hour, minute);
    }

    @Override
    public void onAlarmRepeatSet(RepeatType repeatType) {
        this.draftAlarm.getAlarm().setRepeatType(repeatType);
        userAlarmScheduler.setNextFiringDayString(this.draftAlarm.getAlarm());

        if (RepeatType.CUSTOM.equals(repeatType)) {
            new SetDaysDialog().show(this.fragmentManager, "setDays");
        }
    }

    @Override
    public void onAlarmDaysSet(int[] days) {
        this.draftAlarm.getAlarm().setSelectedDays(days);
        userAlarmScheduler.setNextFiringDayString(this.draftAlarm.getAlarm());
    }

    @Override
    public void onAlarmDurationSet(long duration) {
        this.draftAlarm.getAlarm().setDuration(duration);
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
        intent.putExtra(COLOR, AlertUtils.getTheme(route));
        startActivity(intent);
        finish();
    }

    @Override
    public void onAlarmDirectionSet(Direction direction) {
        this.draftAlarm.setDirection(direction);
        this.draftAlarm.setEndpoints(new ArrayList<>());

        // Once the direction is set, query for endpoints.
        String routeId = this.draftAlarm.getAlarm().getRouteId();
        String directionId = Long.toString(direction.getDirectionId());
        new QueryEndpointsTask(this, database).execute(routeId, directionId);
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

        if (this.draftAlarm.getEndpoints().isEmpty()) {
            this.draftAlarm.setEndpoints(this.endpointList);
        }

        // Once endpoints are received, query for stops.
        String routeId = this.draftAlarm.getAlarm().getRouteId();
        new QueryStopsTask(this, database).execute(routeId);
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

    @Override
    public void onInsertAlarm(long insertedAlarmId) {
        // TODO can do this in the InsertAlarmTask
        this.draftAlarm.getAlarm().setId(insertedAlarmId);
        AlarmEndpoint[] alarmEndpoints = AlarmUtils.createAlarmEndpoints(this.draftAlarm);
        new InsertAlarmEndpointsTask(database).execute(alarmEndpoints);
        this.alarmManagerHelper.scheduleUserAlarm(this.draftAlarm);
        finish();
    }

    @Override
    public void onUpdateAlarm(UserAlarmWithRelations alarmWithRelations) {
        // TODO can do this in the UpdateAlarmTask
        if (!this.alarm.getEndpoints().equals(alarmWithRelations.getEndpoints())) {
            // Delete the endpoints for the existing alarm.
            AlarmEndpoint[] oldAlarmEndpoints = this.alarm.getAlarmEndpoints().toArray(new AlarmEndpoint[]{});
            new DeleteAlarmEndpointsTask(database).execute(oldAlarmEndpoints);

            // Insert new endpoints for the updated alarm.
            AlarmEndpoint[] newAlarmEndpoints = AlarmUtils.createAlarmEndpoints(alarmWithRelations);
            new InsertAlarmEndpointsTask(database).execute(newAlarmEndpoints);
        }

        // Schedule the alarm.
        if (alarmWithRelations.getAlarm().isActive()) {
            this.alarmManagerHelper.scheduleUserAlarm(alarmWithRelations);
        } else {
            this.alarmManagerHelper.cancelUserAlarm(alarmWithRelations);
        }

        finish();
    }

    @Override
    public void onReceiveAlarms(List<UserAlarmWithRelations> alarms) {}

    @Override
    public void onDeleteAlarm(UserAlarmWithRelations alarmWithRelations) {}
}
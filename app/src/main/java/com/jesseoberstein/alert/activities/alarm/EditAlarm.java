package com.jesseoberstein.alert.activities.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.interfaces.data.StopsReceiver;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.tasks.InsertAlarmTask;
import com.jesseoberstein.alert.tasks.QueryDirectionsTask;
import com.jesseoberstein.alert.tasks.UpdateAlarmTask;
import com.jesseoberstein.alert.utils.AlarmManagerHelper;
import com.jesseoberstein.alert.utils.Constants;
import com.jesseoberstein.alert.utils.LiveDataUtils;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;
import com.jesseoberstein.alert.viewmodels.DraftAlarmViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import lombok.Getter;

import static com.jesseoberstein.alert.utils.ActivityUtils.setDrawableColor;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.CURRENT_TAB;

@AndroidEntryPoint
public class EditAlarm extends AppCompatActivity implements OnDialogClick, StopsReceiver, DirectionsReceiver, AlarmReceiver {

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

    DraftAlarmViewModel viewModel;

    private ViewPager pager;
    private boolean isAlarmUpdate;

    @Getter
    private List<Stop> stopList;

    @Getter
    private List<Direction> directionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getIntent().getIntExtra(COLOR, R.style.AlarmSettingsDark_Default));

        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(this).get(DraftAlarmViewModel.class);

        UserAlarmWithRelations alarmWithRelations = (UserAlarmWithRelations) getIntent().getSerializableExtra(ALARM);
        this.isAlarmUpdate = !Objects.isNull(alarmWithRelations);
        UserAlarm alarm = this.isAlarmUpdate ? alarmWithRelations.getAlarm().toBuilder().build() : new UserAlarm();

//        boolean isActivityRestarted = Optional.ofNullable(this.draftAlarm).isPresent();

//        if (this.isAlarmUpdate && !isActivityRestarted) {
//            this.draftAlarm = new UserAlarmWithRelations(this.alarm);
//            Optional.of(this.alarm.getDirection()).ifPresent(this::onAlarmDirectionSet);
//            Optional.of(this.alarm.getStop()).ifPresent(this::onAlarmStopSet);
//            this.onAlarmEndpointsSet(this.alarm.getEndpoints());
//        } else if (this.draftAlarm == null) {
//            this.draftAlarm = new UserAlarmWithRelations();
//            userAlarmScheduler.setDefaultAlarmTime(this.draftAlarm.getAlarm());
//        }

        if (!this.isAlarmUpdate) {
            userAlarmScheduler.setDefaultAlarmTime(alarm);
        }

        this.viewModel.getDraftAlarm().setValue(alarm);

        Optional.ofNullable(alarm.getRouteId())
                .ifPresent(viewModel::loadRoute);

        Optional.ofNullable(alarm.getStopId())
                .ifPresent(viewModel::loadStop);

        Optional.ofNullable(alarm.getDirectionId())
                .ifPresent(directionId -> viewModel.loadDirection(directionId, alarm.getRouteId()));

        setUpView();

        // Query for directions if the activity was restarted with a route change.
        Optional.ofNullable(alarm.getRouteId())
            .ifPresent(routeId -> new QueryDirectionsTask(this, database).execute(routeId));

        viewModel.getDraftAlarm().observe(this, userAlarm -> {
            userAlarmScheduler.setAlarmTime(userAlarm);
            userAlarmScheduler.setNextFiringDayString(userAlarm);
        });

        // Once the route is set, restart the activity to apply the selected route'
        viewModel.getRoute().observe(this, route -> {
//            Intent intent = getIntent();
//            intent.putExtra(CURRENT_TAB, pager.getCurrentItem());
//            intent.putExtra(COLOR, AlertUtils.getTheme(route));
//            startActivity(intent);
//            finish();
        });
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
        LiveDataUtils.observeOnce(this, viewModel.getDraftAlarm(), alarm -> {
            if (alarm.isValid()) {
                if (this.isAlarmUpdate) {
                    new UpdateAlarmTask(this, database).execute(alarm);
                } else {
                    new InsertAlarmTask(this, database).execute(alarm);
                }
            } else {
                createValidationSnackbar(alarm).show();
            }
        });

        return false;
    }

    /**
     * Create a snackbar to show for an alarm validation error.
     * @return A snackbar containing the validation error and an action button for fixing it.
     */
    private Snackbar createValidationSnackbar(UserAlarm alarm) {
        Snackbar snackbar = Snackbar.make(pager, getValidationErrorMessage(alarm), Snackbar.LENGTH_INDEFINITE);
        int sectionIdToFix = getSectionIdToFix(alarm);

        if (sectionIdToFix > -1) {
            snackbar.setAction(R.string.fix, view -> {
                findViewById(sectionIdToFix).performClick();
                snackbar.dismiss();
            });
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
    private int getSectionIdToFix(UserAlarm alarm) {
        switch (alarm.getErrors().get(0)) {
            case Constants.CUSTOM_REPEAT_TYPE:
                return R.id.alarmSettings_repeat;
            case Constants.ROUTE:
                return R.id.alarmSettings_route;
            case Constants.DIRECTION_ID:
                return R.id.alarmSettings_direction;
            case Constants.STOP_ID:
                return R.id.alarmSettings_stop;
//            case Constants.ENDPOINTS:
//                return R.id.alarmSettings_endpoints;
        }
        return -1;
    }

    /**
     * Get the error message shown for the section that has failed validation.
     * @return An error message based on an invalid alarm property.
     */
    private int getValidationErrorMessage(UserAlarm alarm) {
        switch (alarm.getErrors().get(0)) {
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

    @Override
    public void onReceiveDirections(List<Direction> directions) {
        this.directionList = Collections.unmodifiableList(new ArrayList<>(directions));
    }

    @Override
    public void onReceiveStops(List<Stop> stops) {
        this.stopList = Collections.unmodifiableList(new ArrayList<>(stops));

        // If the stop list doesn't contain the selected stop, null out the alarm's stop.
//        if (!this.stopList.contains(this.draftAlarm.getStop())) {
//            this.draftAlarm.setStop(null);
//        }
    }

    @Override
    public void onInsertAlarm(long insertedAlarmId) {
        UserAlarm alarm = viewModel.getDraftAlarm().getValue();

        // TODO can do this in the InsertAlarmTask
        alarm.setId(insertedAlarmId);
//        AlarmEndpoint[] alarmEndpoints = AlarmUtils.createAlarmEndpoints(this.draftAlarm);
//        new InsertAlarmEndpointsTask(database).execute(alarmEndpoints);
        this.alarmManagerHelper.scheduleUserAlarm(alarm);
        finish();
    }

    @Override
    public void onUpdateAlarm(UserAlarm alarm) {
        if (alarm.isActive()) {
            this.alarmManagerHelper.scheduleUserAlarm(alarm);
        } else {
            this.alarmManagerHelper.cancelUserAlarm(alarm);
        }

        finish();
    }

    @Override
    public void onReceiveAlarms(List<UserAlarmWithRelations> alarms) {}

    @Override
    public void onDeleteAlarm(UserAlarmWithRelations alarmWithRelations) {}
}
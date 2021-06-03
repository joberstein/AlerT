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
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.tasks.InsertAlarmTask;
import com.jesseoberstein.alert.tasks.UpdateAlarmTask;
import com.jesseoberstein.alert.utils.AlarmManagerHelper;
import com.jesseoberstein.alert.utils.LiveDataUtils;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;
import com.jesseoberstein.alert.viewmodels.DraftAlarmViewModel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.jesseoberstein.alert.utils.ActivityUtils.setDrawableColor;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.CURRENT_TAB;

@AndroidEntryPoint
public class EditAlarm extends AppCompatActivity implements OnDialogClick, AlarmReceiver {

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

        Optional.ofNullable(alarm.getDirectionId())
                .ifPresent(directionId -> viewModel.loadDirection(directionId, alarm.getRouteId()));

        Optional.ofNullable(alarm.getStopId())
                .ifPresent(stopId -> viewModel.loadStop(stopId, alarm.getRouteId(), alarm.getDirectionId().intValue()));

        setUpView();

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
            Snackbar errorSnackbar = getErrorSnackbar(alarm);

            if (errorSnackbar != null) {
                errorSnackbar.show();
                return;
            }

            if (this.isAlarmUpdate) {
                new UpdateAlarmTask(this, database).execute(alarm);
            } else {
                new InsertAlarmTask(this, database).execute(alarm);
            }
        });

        return false;
    }

    private Snackbar getErrorSnackbar(UserAlarm alarm) {
        if (this.viewModel.getRoute().getValue() == null) {
            return buildErrorSnackbar(R.string.route_invalid, R.id.alarmSettings_route);
        }

        if (this.viewModel.getDirection().getValue() == null) {
            return buildErrorSnackbar(R.string.direction_invalid, R.id.alarmSettings_direction);
        }

        if (this.viewModel.getStop().getValue() == null) {
            return buildErrorSnackbar(R.string.stop_invalid, R.id.alarmSettings_stop);
        }

        if (RepeatType.CUSTOM.equals(alarm.getRepeatType()) && !alarm.getSelectedDays().isAnyDaySelected()) {
            return buildErrorSnackbar(R.string.repeat_custom_invalid, R.id.alarmSettings_repeat);
        }

        return null;
    }

    /**
     * Build a snackbar with the given error message and link to a view to fix the error.
     */
    private Snackbar buildErrorSnackbar(int errorMessageId, int viewId) {
        Snackbar snackbar = Snackbar.make(pager, errorMessageId, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(R.string.fix, view -> {
            findViewById(viewId).performClick();
            snackbar.dismiss();
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.alert_red, null));

        TextView snackbarTextView = snackbar.getView().findViewById(R.id.snackbar_text);
        snackbarTextView.setTextColor(getResources().getColor(R.color.white, null));
        return snackbar;
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
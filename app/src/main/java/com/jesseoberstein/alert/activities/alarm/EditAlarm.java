package com.jesseoberstein.alert.activities.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.DraftAlarmThemeAdapter;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.utils.AlarmManagerHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;
import com.jesseoberstein.alert.viewmodels.DraftAlarmViewModel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.jesseoberstein.alert.utils.Constants.ALARM;

@AndroidEntryPoint
public class EditAlarm extends AppCompatActivity implements OnDialogClick, AlarmReceiver {

    @Inject
    UserAlarmScheduler userAlarmScheduler;

    @Inject
    AlarmManagerHelper alarmManagerHelper;

    @Inject
    AppDatabase database;

    DraftAlarmViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(this).get(DraftAlarmViewModel.class);

        UserAlarmWithRelations alarmWithRelations = (UserAlarmWithRelations) getIntent().getSerializableExtra(ALARM);
        boolean isAlarmUpdate = !Objects.isNull(alarmWithRelations);
        UserAlarm alarm = isAlarmUpdate ? alarmWithRelations.getAlarm().toBuilder().build() : new UserAlarm();

        if (!isAlarmUpdate) {
            userAlarmScheduler.setDefaultAlarmTime(alarm);
        }

        this.viewModel.getDraftAlarm().setValue(alarm);

        Optional.ofNullable(alarm.getRouteId())
                .ifPresent(viewModel::loadRoute);

        Optional.ofNullable(alarm.getDirectionId())
                .ifPresent(directionId -> viewModel.loadDirection(directionId, alarm.getRouteId()));

        Optional.ofNullable(alarm.getStopId())
                .ifPresent(stopId -> viewModel.loadStop(stopId, alarm.getRouteId(), alarm.getDirectionId().intValue()));

        viewModel.getDraftAlarm().observe(this, userAlarm -> {
            userAlarmScheduler.setAlarmTime(userAlarm);
            userAlarmScheduler.setNextFiringDayString(userAlarm);
        });

        setTheme(R.style.AlarmSettingsDark_Default);
        this.viewModel.getThemeId().observe(this, this::setTheme);

        setContentView(R.layout.activities_edit_alarm);

        DraftAlarmThemeAdapter themeAdapter = new DraftAlarmThemeAdapter(this, isAlarmUpdate);
        ViewPager2 pager = findViewById(R.id.edit_alarm_theme_pager);
        pager.setAdapter(themeAdapter);
        pager.setCurrentItem(0);

        this.viewModel.getThemeId().observe(this, themeId -> {
            int itemIndex = themeAdapter.getItemIndex(themeId);
            pager.setCurrentItem(itemIndex, false);
        });
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
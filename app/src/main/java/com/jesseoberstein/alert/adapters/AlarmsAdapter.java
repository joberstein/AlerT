package com.jesseoberstein.alert.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.databinding.ViewAlarmsBinding;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.AlertUtils.getHexColor;
import static com.jesseoberstein.alert.utils.AlertUtils.getTextColorForWhiteBackground;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.COLOR;

public class AlarmsAdapter extends BaseRecyclerAdapter<UserAlarmWithRelations> {

    public AlarmsAdapter(int layout, List<UserAlarmWithRelations> alarms) {
        super(layout, alarms);
    }

    public List<UserAlarmWithRelations> getAlarms() {
        return this.getItems();
    }

    @Override
    public long getItemId(int position) {
        UserAlarm alarm = this.getItems().get(position).getAlarm();
        return alarm.getId();
    }

    @Override
    public int getItemViewType(int position) {
        UserAlarm alarm = this.getItems().get(position).getAlarm();
        return Long.valueOf(alarm.getId()).intValue();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        ViewAlarmsBinding binding = DataBindingUtil.bind(this.getInflatedView(parentView));
        return new AlarmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        AlarmViewHolder alarmViewHolder = (AlarmViewHolder) holder;
        View view = alarmViewHolder.itemView;
        UserAlarmWithRelations alarmWithRelations = getAlarms().get(position);
        UserAlarm alarm = alarmWithRelations.getAlarm();
        Route alarmRoute = alarmWithRelations.getRoute();

        List<Integer> daysList = Arrays.stream(alarm.getSelectedDays().toIntArray()).boxed().collect(Collectors.toList());
        int activeColor = Color.parseColor(getHexColor(getTextColorForWhiteBackground(alarmRoute)));
        int inactiveColor = view.getContext().getResources().getColor(R.color.alarm_off, null);
        DaysAdapter daysAdapter = new DaysAdapter(R.layout.list_alarms_days, daysList, activeColor, inactiveColor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayout.HORIZONTAL, false);

        alarmViewHolder.days.setAdapter(daysAdapter);
        alarmViewHolder.days.setLayoutManager(layoutManager);

        alarmViewHolder.binding.setAlarm(alarm);
        alarmViewHolder.binding.setStop(alarmWithRelations.getStop());
        alarmViewHolder.binding.setDirection(alarmWithRelations.getDirection());
        alarmViewHolder.binding.setActiveStatusTextColor(alarm.isActive() ? activeColor : inactiveColor);
        alarmViewHolder.binding.setRouteColor(Color.parseColor(getHexColor(alarmRoute.getColor())));
        alarmViewHolder.binding.setRouteTextColor(Color.parseColor(getHexColor(alarmRoute.getTextColor())));

        view.setOnClickListener(v -> onAlarmClick(alarmViewHolder.context, alarmWithRelations));
    }

    static class AlarmViewHolder extends BaseViewHolder {
        private Context context;
        private ViewAlarmsBinding binding;
        private RecyclerView days;

        AlarmViewHolder(ViewAlarmsBinding binding) {
            super(binding.getRoot());
            View view = binding.getRoot();
            this.context = view.getContext();
            this.days = view.findViewById(R.id.alarm_days);
            this.binding = binding;
        }
    }

    /**
     * Start the 'EditAlarm' activity when an alarm view is clicked, passing along the alarm id.
     * @param context The view context.
     * @param alarm The clicked alarm.
     */
    private void onAlarmClick(Context context, UserAlarmWithRelations alarm) {
        Intent intent = new Intent(context, EditAlarm.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(ALARM, alarm);
        intent.putExtra(COLOR, AlertUtils.getTheme(alarm.getRoute()));
        context.startActivity(intent);
    }
}
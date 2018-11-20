package com.jesseoberstein.alert.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.databinding.ViewAlarmsBinding;
import com.jesseoberstein.alert.fragments.dialog.alarms.RemoveAlarmDialog;
import com.jesseoberstein.alert.interfaces.AlarmAdapterInteractor;
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
        UserAlarmWithRelations alarmWithRelations = getAlarms().get(position);
        UserAlarm alarm = alarmWithRelations.getAlarm();
        Route alarmRoute = alarmWithRelations.getRoute();
        int activeDayColor = Color.parseColor(getHexColor(getTextColorForWhiteBackground(alarmRoute)));
        int inactiveDayColor = alarmViewHolder.context.getResources().getColor(R.color.alarm_off, null);
        int routeColor = Color.parseColor(getHexColor(alarmRoute.getColor()));

        alarmViewHolder.binding.setAlarm(alarm);
        alarmViewHolder.binding.setStop(alarmWithRelations.getStop());
        alarmViewHolder.binding.setDirection(alarmWithRelations.getDirection());
        alarmViewHolder.binding.setRouteColor(routeColor);
        alarmViewHolder.binding.setRouteTextColor(Color.parseColor(getHexColor(alarmRoute.getTextColor())));

        setAlarmStatusView(alarmViewHolder, alarmWithRelations, routeColor, inactiveDayColor);
        setAlarmDaysView(alarmViewHolder, alarm, activeDayColor, inactiveDayColor);

        View view = alarmViewHolder.itemView;
        view.setOnClickListener(v -> onAlarmClick(alarmViewHolder.context, alarmWithRelations));
        view.setOnLongClickListener(v -> onAlarmLongClick(alarmViewHolder.context, alarmWithRelations));
    }

    static class AlarmViewHolder extends BaseViewHolder {
        private Context context;
        private ViewAlarmsBinding binding;
        private RecyclerView days;
        private ImageView status;

        AlarmViewHolder(ViewAlarmsBinding binding) {
            super(binding.getRoot());
            View view = binding.getRoot();
            this.context = view.getContext();
            this.days = view.findViewById(R.id.alarm_days);
            this.status = view.findViewById(R.id.alarm_status);
            this.binding = binding;
        }
    }

    /**
     * Set the day view display for this alarm.
     */
    private void setAlarmDaysView(AlarmViewHolder alarmViewHolder, UserAlarm alarm, int activeColor, int inactiveColor) {
        List<Integer> daysList = Arrays.stream(alarm.getSelectedDays().toIntArray()).boxed().collect(Collectors.toList());
        DaysAdapter existingAdapter = (DaysAdapter) alarmViewHolder.days.getAdapter();

        if (existingAdapter == null || !existingAdapter.getItems().equals(daysList)) {
            DaysAdapter daysAdapter = new DaysAdapter(R.layout.list_alarms_days, daysList, activeColor, inactiveColor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(alarmViewHolder.context, LinearLayout.HORIZONTAL, false);
            alarmViewHolder.days.setAdapter(daysAdapter);
            alarmViewHolder.days.setLayoutManager(layoutManager);
        }
    }

    /**
     * Set the status view for this alarm.
     */
    private void setAlarmStatusView(AlarmViewHolder alarmViewHolder, UserAlarmWithRelations alarmWithRelations, int activeColor, int inactiveColor) {
        boolean isActive = alarmWithRelations.getAlarm().isActive();
        int statusDrawable = isActive ? R.drawable.ic_notifications_on : R.drawable.ic_notifications_off;
        int statusIconColor = isActive ? activeColor : inactiveColor;
        alarmViewHolder.status.setImageDrawable(alarmViewHolder.context.getDrawable(statusDrawable));
        alarmViewHolder.status.setColorFilter(statusIconColor, PorterDuff.Mode.SRC_IN);
        alarmViewHolder.status.setOnClickListener(v -> onAlarmStatusClick(alarmViewHolder.context, alarmWithRelations));
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

    /**
     * On long click, confirm if the user would like to delete the alarm.
     * @param context The view context.
     * @param alarmWithRelations The long-clicked alarm.
     */
    private boolean onAlarmLongClick(Context context, UserAlarmWithRelations alarmWithRelations) {
        RemoveAlarmDialog dialog = new RemoveAlarmDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALARM, alarmWithRelations);

        dialog.setArguments(bundle);
        dialog.show(((Activity) context).getFragmentManager(), "RemoveAlarmDialog");
        return true;
    }

    /**
     * On alarm status icon click, toggle the alarm status.
     * @param context
     * @param alarmWithRelations
     */
    private void onAlarmStatusClick(Context context, UserAlarmWithRelations alarmWithRelations) {
        UserAlarm alarm = alarmWithRelations.getAlarm();
        alarm.setActive(!alarm.isActive());
        alarmWithRelations.setAlarm(alarm);
        updateItem(alarmWithRelations);
        ((AlarmAdapterInteractor) context).onAlarmStatusToggle(alarmWithRelations);
    }
}
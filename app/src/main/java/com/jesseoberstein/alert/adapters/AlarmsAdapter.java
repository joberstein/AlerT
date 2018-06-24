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
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.AlertUtils.*;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;

public class AlarmsAdapter extends BaseRecyclerAdapter<UserAlarm> {
    private ViewAlarmsBinding binding;

    public AlarmsAdapter(int layout, List<UserAlarm> alarms) {
        super(layout, alarms);
    }

    @Override
    public long getItemId(int position) {
        return this.getItems().get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return Long.valueOf(this.getItems().get(position).getId()).intValue();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        this.binding = DataBindingUtil.bind(this.getInflatedView(parentView));
        return new AlarmViewHolder(this.binding.getRoot());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        AlarmViewHolder alarmViewHolder = (AlarmViewHolder) holder;
        View view = alarmViewHolder.itemView;
        UserAlarm alarm = getItems().get(position);

        List<Integer> daysList = Arrays.stream(alarm.getSelectedDays().toIntArray()).boxed().collect(Collectors.toList());
        int activeColor = Color.parseColor(getHexColor(getTextColorForWhiteBackground(alarm)));
        int inactiveColor = view.getContext().getResources().getColor(R.color.alarm_off, null);
        DaysAdapter daysAdapter = new DaysAdapter(R.layout.list_alarms_days, daysList, activeColor, inactiveColor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayout.HORIZONTAL, false);

        alarmViewHolder.days.setAdapter(daysAdapter);
        alarmViewHolder.days.setLayoutManager(layoutManager);

        this.binding.setAlarm(alarm);
        this.binding.setActiveStatusTextColor(alarm.isActive() ? activeColor : inactiveColor);
        this.binding.setRouteColor(Color.parseColor(getHexColor(alarm.getRoute().getColor())));
        this.binding.setRouteTextColor(Color.parseColor(getHexColor(alarm.getRoute().getTextColor())));

        view.setOnClickListener(v -> onAlarmClick(alarmViewHolder.context, alarm));
    }

    static class AlarmViewHolder extends BaseViewHolder {
        private Context context;
        private RecyclerView days;

        AlarmViewHolder(View alarmView) {
            super(alarmView);
            context = alarmView.getContext();
            days = alarmView.findViewById(R.id.alarm_days);
        }
    }

    /**
     * Start the 'EditAlarm' activity when an alarm view is clicked, passing along the alarm id.
     * @param context The view context.
     * @param alarm The clicked alarm.
     */
    private void onAlarmClick(Context context, UserAlarm alarm) {
        Intent intent = new Intent(context, EditAlarm.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(ALARM_ID, alarm.getId());
        context.startActivity(intent);
    }
}
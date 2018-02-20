package com.jesseoberstein.alert.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlarmsAdapter extends BaseRecyclerAdapter<UserAlarm> {

    public AlarmsAdapter(int layout, List<UserAlarm> alarms) {
        super(layout, alarms);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        return new AlarmViewHolder(this.getInflatedView(parentView));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        AlarmViewHolder alarmHolder = (AlarmViewHolder) holder;
        UserAlarm alarm = getItems().get(position);
        alarmHolder.time.setText(alarm.getTime());
        alarmHolder.name.setText(alarm.getNickname());
        alarmHolder.stop.setText(alarm.getStation());
        alarmHolder.direction.setText(alarm.getDirection());

        int alarmStatusId = alarm.isActive() ? R.drawable.circle_light_green : R.drawable.circle_light_gray;
        alarmHolder.status.setImageResource(alarmStatusId);

        List<Integer> days = Arrays.stream(alarm.getWeekdaysUnpadded()).boxed().collect(Collectors.toList());
        alarmHolder.days.setAdapter(new DaysAdapter(R.layout.alarm_day, days));
        alarmHolder.days.setLayoutManager(new LinearLayoutManager(alarmHolder.context, LinearLayout.HORIZONTAL, false));

        alarmHolder.view.setOnClickListener(view -> removeItem(position));
    }

    static class AlarmViewHolder extends BaseViewHolder {
        private Context context;
        private View view;
        private TextView time;
        private TextView name;
        private TextView stop;
        private TextView direction;
        private ImageView status;
        private RecyclerView days;

        AlarmViewHolder(View alarmView) {
            super(alarmView);
            context = alarmView.getContext();
            view = alarmView;
            time = (TextView) alarmView.findViewById(R.id.alarm_time);
            name = (TextView) alarmView.findViewById(R.id.alarm_name);
            stop = (TextView) alarmView.findViewById(R.id.alarm_stop);
            direction = (TextView) alarmView.findViewById(R.id.alarm_direction);
            status = (ImageView) alarmView.findViewById(R.id.alarm_status);
            days = (RecyclerView) alarmView.findViewById(R.id.alarm_days);
        }
    }
}
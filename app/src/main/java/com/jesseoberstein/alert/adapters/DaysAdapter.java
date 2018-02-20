package com.jesseoberstein.alert.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesseoberstein.alert.R;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

public class DaysAdapter extends BaseRecyclerAdapter<Integer> {

    DaysAdapter(int layout, List<Integer> days) {
        super(layout, days);
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DayViewHolder(this.getInflatedView(parent));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        DayViewHolder dayHolder = (DayViewHolder) holder;
        Integer day = getItems().get(position);
        TextView dayView = day == 1 ? dayHolder.selectedDay : dayHolder.unselectedDay;
        dayView.setText(getDayText(position + 1));
        dayView.setVisibility(View.VISIBLE);
    }

    private String getDayText(int dayIndex) {
        switch (dayIndex) {
            case SUNDAY:    return "S";
            case MONDAY:    return "M";
            case TUESDAY:   return "T";
            case WEDNESDAY: return "W";
            case THURSDAY:  return "R";
            case FRIDAY:    return "F";
            case SATURDAY:  return "S";
            default:        return "?";
        }
    }

    static class DayViewHolder extends BaseViewHolder {
        private TextView selectedDay;
        private TextView unselectedDay;

        DayViewHolder(View dayView) {
            super(dayView);
            selectedDay = (TextView) dayView.findViewById(R.id.alarm_day_selected);
            unselectedDay = (TextView) dayView.findViewById(R.id.alarm_day_unselected);
        }
    }
}
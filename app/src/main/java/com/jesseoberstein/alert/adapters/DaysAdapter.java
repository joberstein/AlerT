package com.jesseoberstein.alert.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.jesseoberstein.alert.databinding.ViewAlarmsDaysBinding;

import java.util.List;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

public class DaysAdapter extends BaseRecyclerAdapter<Integer> {
    private ViewAlarmsDaysBinding binding;
    private int selectedColor;
    private int unselectedColor;

    DaysAdapter(int layout, List<Integer> dayList, int selectedColor, int unselectedColor) {
        super(layout, dayList);
        this.selectedColor = selectedColor;
        this.unselectedColor = unselectedColor;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.binding = DataBindingUtil.bind(this.getInflatedView(parent));
        return new BaseViewHolder(this.binding.getRoot());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        boolean isDaySelected = this.getItems().get(position) == 1;
        this.binding.setDayLetter(getDayText(position + 1));
        this.binding.setDayTextColor(isDaySelected ? this.selectedColor : this.unselectedColor);
        this.binding.setDayTypeface(isDaySelected ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
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
}
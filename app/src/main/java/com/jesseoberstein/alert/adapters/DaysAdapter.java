package com.jesseoberstein.alert.adapters;

import android.graphics.Typeface;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

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
    private int selectedColor;
    private int unselectedColor;

    DaysAdapter(int layout, List<Integer> dayList, int selectedColor, int unselectedColor) {
        super(layout, dayList);
        this.selectedColor = selectedColor;
        this.unselectedColor = unselectedColor;
    }

    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewAlarmsDaysBinding binding = DataBindingUtil.bind(this.getInflatedView(parent));
        return new DaysViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        DaysViewHolder daysHolder = (DaysViewHolder) holder;
        boolean isDaySelected = this.getItems().get(position) == 1;
        daysHolder.binding.setDayLetter(getDayText(position + 1));
        daysHolder.binding.setDayTextColor(isDaySelected ? this.selectedColor : this.unselectedColor);
        daysHolder.binding.setDayTypeface(isDaySelected ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    static class DaysViewHolder extends BaseViewHolder {
        private ViewAlarmsDaysBinding binding;

        DaysViewHolder(ViewAlarmsDaysBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
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
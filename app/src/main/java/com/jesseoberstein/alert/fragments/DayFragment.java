package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.ToggleOnClick;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DayFragment extends AlarmBaseFragment {

    public static DayFragment newInstance(int page) {
        return (DayFragment) AlarmBaseFragment.newInstance(page, new DayFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_day, container, false);
        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_2);

        CustomListAdapter weekdaysAdapter = new CustomListAdapter(view.getContext(), R.layout.list_weekdays, generateWeekdays(view));
        ListView listView = (ListView) view.findViewById(R.id.weekdays_list);
        listView.setAdapter(weekdaysAdapter);
        listView.setOnItemClickListener(new ToggleOnClick());

        return view;
    }

    ArrayList<CustomListItem> generateWeekdays(View view) {
        return Arrays.stream(view.getContext().getResources().getStringArray(R.array.weekdays))
                .map(CustomListItem::buildSimpleTextItem)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

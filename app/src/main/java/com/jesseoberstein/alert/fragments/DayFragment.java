package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.listeners.ToggleColorOnClick;
import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.DAYS;

public class DayFragment extends AlarmBaseFragment implements OnAlarmSubmit {
    private ListView daysListView;

    public static DayFragment newInstance(int page) {
        return (DayFragment) AlarmBaseFragment.newInstance(page, new DayFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_day, container, false);
        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_2);

        CustomListAdapter weekdaysAdapter = new CustomListAdapter(view.getContext(), R.layout.list_weekdays, generateWeekdays(view));
        daysListView = (ListView) view.findViewById(R.id.weekdays_list);
        daysListView.setAdapter(weekdaysAdapter);

        int themeColor = getActivity().getIntent().getExtras().getInt(COLOR);
        daysListView.setOnItemClickListener(new ToggleColorOnClick(themeColor, R.color.white, getContext()));

        return view;
    }

    ArrayList<CustomListItem> generateWeekdays(View view) {
        return Arrays.stream(view.getContext().getResources().getStringArray(R.array.weekdays))
                .map(CustomListItem::buildSimpleTextItem)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Bundle onAlarmSubmit() {
        Bundle bundle = new Bundle();
        ArrayList<String> selectedDays = IntStream.range(0, daysListView.getChildCount())
                .mapToObj(i -> (CheckedTextView) daysListView.getChildAt(i))
                .filter(CheckedTextView::isChecked)
                .map(dayView -> dayView.getText().toString())
                .collect(Collectors.toCollection(ArrayList::new));
        bundle.putStringArrayList(DAYS, selectedDays);
        return bundle;
    }
}

package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.adapters.CustomItemsAdapter;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.listeners.ToggleColorOnClick;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.utils.Constants.COLOR;

public class DayFragment extends AlarmBaseFragment implements OnAlarmSubmit {
    UserAlarm alarm;
    private ListView daysListView;
    private boolean initialView = true;

    public static DayFragment newInstance(int page) {
        return (DayFragment) AlarmBaseFragment.newInstance(page, new DayFragment());
    }

    /**
     * Called when the UI is visible to the user. I'm taking advantage of this method's callback
     * to populate the initial persisted selection of days for an alarm, since the 'daysListView'
     * doesn't have any children in onCreate (off-screen).
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (initialView && daysListView != null && daysListView.getChildCount() > 0) {
            int[] selectedDays = alarm.getWeekdays();
            IntStream.range(0, daysListView.getChildCount())
                    .filter(i -> {
                        boolean isChecked = ((CheckedTextView) daysListView.getChildAt(i)).isChecked();
                        return (selectedDays[i + 1] == 1) != isChecked;
                    })
                    .forEach(i -> {
                        int weekdayOffset = i + 1;
                        selectedDays[weekdayOffset] = (selectedDays[weekdayOffset] == 1) ? 1 : 0;
                        long itemId = daysListView.getAdapter().getItemId(i);
                        daysListView.performItemClick(daysListView.getChildAt(i), i, itemId);
                    });

            alarm.setWeekdays(selectedDays);
            initialView = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_day, container, false);
        alarm = ((EditAlarm) getActivity()).getAlarm();

        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_2);

        CustomItemsAdapter adapter = new CustomItemsAdapter(view.getContext(), R.layout.list_weekdays, generateWeekdays(view));
        daysListView = (ListView) view.findViewById(R.id.weekdays_list);
        daysListView.setAdapter(adapter);

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
    public void onAlarmSubmit() {
        int[] selectedDays = alarm.getWeekdays();
        IntStream.range(0, daysListView.getChildCount())
                .forEach(i -> {
                    boolean isChecked = ((CheckedTextView) daysListView.getChildAt(i)).isChecked();
                    int weekdayOffset = i + 1;
                    selectedDays[weekdayOffset] = isChecked ? 1 : 0;
                });

        alarm.setWeekdays(selectedDays);
    }
}

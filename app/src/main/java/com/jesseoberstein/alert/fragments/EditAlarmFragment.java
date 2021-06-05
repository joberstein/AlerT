package com.jesseoberstein.alert.fragments;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.AlarmPagerAdapter;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.tasks.InsertAlarmTask;
import com.jesseoberstein.alert.tasks.UpdateAlarmTask;
import com.jesseoberstein.alert.utils.LiveDataUtils;
import com.jesseoberstein.alert.viewmodels.DraftAlarmViewModel;

import java.util.Optional;
import java.util.stream.IntStream;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.jesseoberstein.alert.utils.ActivityUtils.setDrawableColor;

@AndroidEntryPoint
public class EditAlarmFragment extends Fragment {

    private static final String THEME_ID = "themeId";
    private static final String SETTINGS_PAGE_INDEX = "settingsPageIndex";
    private static final String IS_ALARM_UPDATE = "isAlarmUpdate";

    private int themeId;
    private int settingsPageIndex;
    private boolean isAlarmUpdate;
    private DraftAlarmViewModel viewModel;
    private Snackbar snackbar;

    @Inject
    AppDatabase database;

    public EditAlarmFragment(int themeId, int settingsPageIndex, boolean isUpdate) {
        Bundle args = new Bundle();
        args.putInt(THEME_ID, themeId);
        args.putInt(SETTINGS_PAGE_INDEX, settingsPageIndex);
        args.putBoolean(IS_ALARM_UPDATE, isUpdate);
        this.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(DraftAlarmViewModel.class);
        
        Optional.ofNullable(this.getArguments()).ifPresent(args -> {
            this.themeId = args.getInt(THEME_ID);
            this.settingsPageIndex = args.getInt(SETTINGS_PAGE_INDEX);
            this.isAlarmUpdate = args.getBoolean(IS_ALARM_UPDATE);
        });

        this.viewModel.getDraftAlarmChanged()
                .observe(requireActivity(), alarm -> this.dismissSnackbar());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(requireActivity(), this.themeId);
        LayoutInflater newInflater = inflater.cloneInContext(wrapper);

        return newInflater.inflate(R.layout.fragment_edit_alarm, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Toolbar bar = view.findViewById(R.id.my_toolbar);
        bar.setTitle(R.string.edit_alarm_page);
        bar.setTitle(this.isAlarmUpdate ? R.string.edit_alarm_page : R.string.new_alarm_page);

        // Back Button
        bar.setNavigationIcon(R.drawable.ic_done);
        bar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(requireActivity()));

        // Save Alarm Button
        bar.inflateMenu(R.menu.alarms_save);
        MenuItem save = bar.getMenu().getItem(0);
        setDrawableColor(requireContext(), save.getIcon(), R.attr.menuItemIconColor);
        save.setContentDescription("Save Alarm");
        save.setOnMenuItemClickListener(item -> {
            LiveDataUtils.observeOnce(requireActivity(), this.viewModel.getDraftAlarmChanged(), this::saveAlarm);
            return false;
        });

        // Alarm Settings ViewPager
        AlarmPagerAdapter alarmPagerAdapter = new AlarmPagerAdapter(this.getChildFragmentManager());
        ViewPager pager = view.findViewById(R.id.alarm_settings_pager);
        pager.setAdapter(alarmPagerAdapter);
        pager.setCurrentItem(this.settingsPageIndex);

        // Tabs for Alarm Settings ViewPager
        TabLayout settingsTabs = view.findViewById(R.id.alarm_settings_tabs);
        settingsTabs.setupWithViewPager(pager, true);
        setTabIcons(settingsTabs);
    }

    private void setTabIcons(TabLayout tabs) {
        int[] icons = {R.drawable.ic_alarm, R.drawable.ic_train};

        IntStream.range(0, icons.length).forEach(i -> {
                int icon = icons[i];
                TabLayout.Tab tab = tabs.getTabAt(i);

                Optional.ofNullable(tab).ifPresent(t -> {
                    t.setIcon(icon);
                    t.setId(icon);
                });
            }
        );
    }

    /**
     * If the draft alarm is valid, save it to the database.  If not, show the validation snackbar.
     */
    private void saveAlarm(UserAlarm alarm) {
        validate(alarm);

        Optional.ofNullable(this.snackbar).ifPresent(Snackbar::show);

        if (this.snackbar != null) {
            return;
        }

        if (this.isAlarmUpdate) {
            new UpdateAlarmTask(requireContext(), database).execute(alarm);
        } else {
            new InsertAlarmTask(requireContext(), database).execute(alarm);
        }
    }

    private void dismissSnackbar() {
        Optional.ofNullable(this.snackbar)
                .filter(Snackbar::isShown)
                .ifPresent(Snackbar::dismiss);
    }

    private void validate(UserAlarm alarm) {
        if (alarm.getRouteId() == null) {
            setErrorSnackbar(R.string.route_invalid, R.id.alarmSettings_route);
        } else if (alarm.getDirectionId() == null) {
            setErrorSnackbar(R.string.direction_invalid, R.id.alarmSettings_direction);
        } else if (alarm.getStopId() == null) {
            setErrorSnackbar(R.string.stop_invalid, R.id.alarmSettings_stop);
        } else if (RepeatType.CUSTOM.equals(alarm.getRepeatType()) && !alarm.getSelectedDays().isAnyDaySelected()) {
            setErrorSnackbar(R.string.repeat_custom_invalid, R.id.alarmSettings_repeat);
        }
    }

    /**
     * Build a snackbar with the given error message and link to a view to fix the error.
     */
    private void setErrorSnackbar(int errorMessageId, int targetViewId) {
        this.dismissSnackbar();
        this.snackbar = Snackbar.make(requireView(), errorMessageId, Snackbar.LENGTH_INDEFINITE);

        this.snackbar.setAction(R.string.fix, view -> {
            requireView().findViewById(targetViewId).performClick();
            this.snackbar.dismiss();
        });
        this.snackbar.setActionTextColor(getResources().getColor(R.color.alert_red, null));

        TextView snackbarTextView = this.snackbar.getView().findViewById(R.id.snackbar_text);
        snackbarTextView.setTextColor(getResources().getColor(R.color.white, null));
    }
}
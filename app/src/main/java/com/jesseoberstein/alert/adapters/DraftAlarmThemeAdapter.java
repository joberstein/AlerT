package com.jesseoberstein.alert.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.fragments.EditAlarmFragment;

import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public class DraftAlarmThemeAdapter extends FragmentStateAdapter {

    private boolean isFirstView = true;
    private final boolean isAlarmUpdate;

    private static final int[] THEMES = new int[]{
            R.style.AlarmSettingsDark_Default,
            R.style.AlarmSettingsDark_Orange,
            R.style.AlarmSettingsDark_Blue,
            R.style.AlarmSettingsDark_Red,
            R.style.AlarmSettingsDark_Green,
            R.style.AlarmSettingsDark_Silver,
            R.style.AlarmSettingsDark_Commuter,
            R.style.AlarmSettingsDark_Boat,
            R.style.AlarmSettingsLight_Bus
    };

    public DraftAlarmThemeAdapter(FragmentActivity fragmentActivity, boolean isAlarmUpdate) {
        super(fragmentActivity);
        this.isAlarmUpdate = isAlarmUpdate;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        boolean isValidPosition = position > -1 && position < THEMES.length;
        int theme = isValidPosition ? THEMES[position] : R.style.AlarmSettingsDark_Default;
        int settingsPageIndex = isFirstView ? 0 : 1;
        isFirstView = false;

        return new EditAlarmFragment(theme, settingsPageIndex, this.isAlarmUpdate);
    }

    @Override
    public int getItemCount() {
        return THEMES.length;
    }

    public int getItemIndex(int themeId) {
        return IntStream.range(0, THEMES.length)
                .filter(i -> themeId == THEMES[i])
                .findFirst()
                .orElse(-1);
    }
}

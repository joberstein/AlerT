package com.jesseoberstein.alert.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jesseoberstein.alert.fragments.DayFragment;
import com.jesseoberstein.alert.fragments.SettingsFragment;
import com.jesseoberstein.alert.fragments.TimeFragment;

public class AlarmPagerAdapter extends FragmentPagerAdapter {
    public static int NUM_PAGES = 3;
    private TimeFragment timeFragment;
    private DayFragment dayFragment;
    private SettingsFragment settingsFragment;

    public AlarmPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * Get the count of the total number of views in this adapter.
     * @return The total number of pages.
     */
    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    /**
     * Get a fragment at the given position if there is none.
     * @param position The position to get a fragment for.
     * @return A fragment at the given position.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TimeFragment.newInstance(position);
            case 1:
                return DayFragment.newInstance(position);
            case 2:
                return SettingsFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        switch (position) {
            case 0:
                timeFragment = (TimeFragment) createdFragment;
                break;
            case 1:
                dayFragment = (DayFragment) createdFragment;
                break;
            case 2:
                settingsFragment = (SettingsFragment) createdFragment;
        }

        return createdFragment;
    }

    public TimeFragment getTimeFragment() {
        return timeFragment;
    }

    public DayFragment getDayFragment() {
        return dayFragment;
    }

    public SettingsFragment getSettingsFragment() {
        return settingsFragment;
    }
}

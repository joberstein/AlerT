package com.jesseoberstein.alert.adapters;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jesseoberstein.alert.fragments.MbtaSettingsFragment;
import com.jesseoberstein.alert.fragments.TimeSettingsFragment;

public class AlarmPagerAdapter extends FragmentPagerAdapter {
    private static final int TIME_TAB_PAGE_ID = 0;
    private static final int MBTA_TAB_PAGE_ID = 1;
    private TimeSettingsFragment timeSettingsFragment;
    private MbtaSettingsFragment mbtaSettingsFragment;

    public AlarmPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * Get the count of the total number of views in this adapter.
     * @return The total number of pages.
     */
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Get a fragment at the given position if there is none.
     * @param position The position to get a fragment for.
     * @return A fragment at the given position.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TIME_TAB_PAGE_ID: return TimeSettingsFragment.newInstance(position);
            case MBTA_TAB_PAGE_ID: return MbtaSettingsFragment.newInstance(position);
            default: return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        switch (position) {
            case TIME_TAB_PAGE_ID:
                timeSettingsFragment = (TimeSettingsFragment) createdFragment;
                break;
            case MBTA_TAB_PAGE_ID:
                mbtaSettingsFragment = (MbtaSettingsFragment) createdFragment;
                break;
        }

        return createdFragment;
    }

    public TimeSettingsFragment getTimeSettingsFragment() {
        return timeSettingsFragment;
    }

    public MbtaSettingsFragment getMbtaSettingsFragment() {
        return mbtaSettingsFragment;
    }
}

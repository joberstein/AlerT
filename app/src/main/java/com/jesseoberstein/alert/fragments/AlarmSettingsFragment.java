package com.jesseoberstein.alert.fragments;

import android.os.Bundle;

import com.jesseoberstein.alert.activities.base.BaseFragment;

public abstract class AlarmSettingsFragment extends BaseFragment {

    public static AlarmSettingsFragment newInstance(int page, AlarmSettingsFragment fragment) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }
}

package com.jesseoberstein.alert.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class AlarmSettingsFragment extends Fragment {

    public static AlarmSettingsFragment newInstance(int page, AlarmSettingsFragment fragment) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }
}

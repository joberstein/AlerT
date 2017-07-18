package com.jesseoberstein.alert.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class AlarmBaseFragment extends Fragment {

    public static AlarmBaseFragment newInstance(int page, AlarmBaseFragment fragment) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }
}

package com.jesseoberstein.alert.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public abstract class AlarmSettingsFragment extends Fragment {

    public static AlarmSettingsFragment newInstance(int page, AlarmSettingsFragment fragment) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }

    protected void showDialogFragment(DialogFragment dialog, String tagName) {
        dialog.show(this.getChildFragmentManager(), tagName);
    }
}

package com.jesseoberstein.alert.config.modules.fragment;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.jesseoberstein.alert.activities.base.BaseFragment;
import com.jesseoberstein.alert.config.scopes.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    @FragmentScope
    @Provides
    Context fragmentContext(BaseFragment baseFragment) {
        return baseFragment.getContext();
    }

    @FragmentScope
    @Provides
    FragmentManager fragmentManager(BaseFragment baseFragment) {
        return baseFragment.getActivity().getSupportFragmentManager();
    }
}

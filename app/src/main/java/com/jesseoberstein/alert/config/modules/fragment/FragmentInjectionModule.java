package com.jesseoberstein.alert.config.modules.fragment;

import com.jesseoberstein.alert.config.scopes.FragmentScope;
import com.jesseoberstein.alert.fragments.MbtaSettingsFragment;
import com.jesseoberstein.alert.fragments.TimeSettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentInjectionModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = TimeSettingsModule.class)
    abstract TimeSettingsFragment timeSettingsFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = MbtaSettingsModule.class)
    abstract MbtaSettingsFragment mbtaSettingsFragment();
}

package com.jesseoberstein.alert.config.modules.fragment;

import com.jesseoberstein.alert.activities.base.BaseFragment;
import com.jesseoberstein.alert.config.scopes.FragmentScope;
import com.jesseoberstein.alert.fragments.MbtaSettingsFragment;
import com.jesseoberstein.alert.fragments.TimeSettingsFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = FragmentModule.class)
abstract class MbtaSettingsModule {

    @FragmentScope
    @Binds
    abstract BaseFragment mbtaSettings(MbtaSettingsFragment mbtaSettingsFragment);
}

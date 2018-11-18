package com.jesseoberstein.alert.config.components;

import com.jesseoberstein.alert.MainApplication;
import com.jesseoberstein.alert.config.modules.activity.ActivityInjectionModule;
import com.jesseoberstein.alert.config.modules.application.ApplicationModule;
import com.jesseoberstein.alert.config.modules.dialogFragment.DialogFragmentInjectionModule;
import com.jesseoberstein.alert.config.modules.fragment.FragmentInjectionModule;
import com.jesseoberstein.alert.config.scopes.ApplicationScope;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@ApplicationScope
@Component(modules = {
    ApplicationModule.class,
    AndroidSupportInjectionModule.class,
    ActivityInjectionModule.class,
    FragmentInjectionModule.class,
    DialogFragmentInjectionModule.class
})
public interface ApplicationComponent extends AndroidInjector<MainApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MainApplication> {}
}

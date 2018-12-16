package com.jesseoberstein.alert.config;

import com.jesseoberstein.alert.TestApplication;
import com.jesseoberstein.alert.config.modules.activity.ActivityInjectionModule;
import com.jesseoberstein.alert.config.modules.dialogFragment.DialogFragmentInjectionModule;
import com.jesseoberstein.alert.config.modules.fragment.FragmentInjectionModule;
import com.jesseoberstein.alert.config.modules.receiver.BroadcastReceiverInjectionModule;
import com.jesseoberstein.alert.config.modules.service.ServiceInjectionModule;
import com.jesseoberstein.alert.config.scopes.ApplicationScope;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@ApplicationScope
@Component(modules = {
    TestApplicationModule.class,
    AndroidSupportInjectionModule.class,
    ActivityInjectionModule.class,
    FragmentInjectionModule.class,
    DialogFragmentInjectionModule.class,
    BroadcastReceiverInjectionModule.class,
    ServiceInjectionModule.class,
    TestActivityInjectionModule.class
})
public interface TestApplicationComponent extends AndroidInjector<TestApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<TestApplication> {}
}

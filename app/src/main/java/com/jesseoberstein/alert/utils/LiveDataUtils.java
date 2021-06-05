package com.jesseoberstein.alert.utils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.function.Consumer;

public class LiveDataUtils {

    public static <T> void observeOnce(LifecycleOwner lifecycleOwner, LiveData<T> liveData, Consumer<T> onChanged) {
        liveData.observe(lifecycleOwner, new Observer<T>() {

            @Override
            public void onChanged(T item) {
                liveData.removeObserver(this);
                onChanged.accept(item);
            }
        });
    }
}

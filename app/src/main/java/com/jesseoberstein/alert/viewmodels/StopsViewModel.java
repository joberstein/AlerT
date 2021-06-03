package com.jesseoberstein.alert.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.mbta.Stop;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@HiltViewModel
@EqualsAndHashCode(callSuper = false)
public class StopsViewModel extends ViewModel {

    AppDatabase database;
    SavedStateHandle state;

    @Inject
    public StopsViewModel(SavedStateHandle savedStateHandle, AppDatabase database) {
        this.state = savedStateHandle;
        this.database = database;
    }

    MutableLiveData<String> routeId = new MutableLiveData<>();
    MutableLiveData<Integer> directionId = new MutableLiveData<>();
    MutableLiveData<List<Stop>> stops = new MutableLiveData<>();

    public void loadStops(String routeId) {
        if (this.directionId.getValue() == null) {
            this.routeId.setValue(routeId);
        } else {
            this.loadStops(routeId, this.directionId.getValue());
        }
    }

    public void loadStops(Integer directionId) {
        if (this.routeId.getValue() == null) {
            this.directionId.setValue(directionId);
        } else {
            this.loadStops(this.routeId.getValue(), directionId);
        }
    }

    private void loadStops(String routeId, Integer directionId) {
        if (routeId == null || directionId == null) {
            return;
        }

        boolean isSameRoute = Objects.equals(routeId, this.routeId.getValue());
        boolean isSameDirection = Objects.equals(directionId, this.directionId.getValue());

        if (isSameRoute && isSameDirection && this.stops.getValue() != null) {
            return;
        }

        this.routeId.setValue(routeId);
        this.directionId.setValue(directionId);

        database.stopDao().get(new String[]{routeId})
                .subscribeOn(Schedulers.computation())
                .doOnError(e -> Log.e("StopsViewModel.loadStops", e.getMessage()))
                .subscribe(this.stops::postValue);
    }
}

package com.jesseoberstein.alert.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@HiltViewModel
@EqualsAndHashCode(callSuper = false)
public class DirectionsViewModel extends ViewModel {

    SavedStateHandle state;
    AppDatabase database;

    @Inject
    public DirectionsViewModel(SavedStateHandle savedStateHandle, AppDatabase appDatabase) {
        this.state = savedStateHandle;
        this.database = appDatabase;
    }

    MutableLiveData<String> routeId = new MutableLiveData<>();
    MutableLiveData<List<Direction>> directions = new MutableLiveData<>();

    public void loadDirections(String routeId) {
        boolean isSameRoute = routeId.equals(this.routeId.getValue());

        if (isSameRoute && this.directions.getValue() != null) {
            return;
        }

        this.routeId.setValue(routeId);

        this.database.directionDao().get(routeId)
                .subscribeOn(Schedulers.computation())
                .doOnError(e -> Log.e("DirectionsViewModel.loadDirections", e.getMessage()))
                .subscribe(this.directions::postValue);
    }
}

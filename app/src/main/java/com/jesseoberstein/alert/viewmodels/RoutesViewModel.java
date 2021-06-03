package com.jesseoberstein.alert.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.mbta.Route;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@HiltViewModel
@EqualsAndHashCode(callSuper = false)
public class RoutesViewModel extends ViewModel {

    AppDatabase database;
    SavedStateHandle state;

    @Inject
    public RoutesViewModel(SavedStateHandle savedStateHandle, AppDatabase database) {
        this.state = savedStateHandle;
        this.database = database;
    }

    MutableLiveData<List<Route>> routes = new MutableLiveData<>();

    public void loadRoutes() {
        database.routeDao().getAll()
                .subscribeOn(Schedulers.computation())
                .doOnError(e -> Log.e("RoutesViewModel.loadRoutes", e.getMessage()))
                .subscribe(this.routes::postValue);
    }
}

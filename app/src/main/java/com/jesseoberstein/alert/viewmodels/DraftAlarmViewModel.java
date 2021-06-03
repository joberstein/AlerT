package com.jesseoberstein.alert.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@HiltViewModel
@EqualsAndHashCode(callSuper = false)
public class DraftAlarmViewModel extends ViewModel {

    AppDatabase database;
    SavedStateHandle state;

    @Inject
    public DraftAlarmViewModel(SavedStateHandle savedStateHandle, AppDatabase database) {
        this.state = savedStateHandle;
        this.database = database;
    }

    MutableLiveData<UserAlarm> draftAlarm = new MutableLiveData<>();
    MutableLiveData<String> nickname = new MutableLiveData<>();
    MutableLiveData<Route> route = new MutableLiveData<>();
    MutableLiveData<Stop> stop = new MutableLiveData<>();
    MutableLiveData<Direction> direction = new MutableLiveData<>();

    public void loadRoute(String routeId) {
        this.database.routeDao().get(routeId)
                .subscribeOn(Schedulers.io())
                .subscribe(this.route::postValue);
    }

    public void loadStop(String stopId, String routeId, int directionId) {
        this.database.stopDao().get(stopId, routeId, directionId)
                .subscribeOn(Schedulers.io())
                .subscribe(this.stop::postValue);
    }

    public void loadDirection(long directionId, String routeId) {
        this.database.directionDao().get(directionId, routeId)
                .subscribeOn(Schedulers.io())
                .subscribe(this.direction::postValue);
    }

}

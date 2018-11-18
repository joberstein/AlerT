package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmRouteBinding;
import com.jesseoberstein.alert.interfaces.AlarmRouteSetter;
import com.jesseoberstein.alert.interfaces.data.RoutesReceiver;
import com.jesseoberstein.alert.models.AutoComplete;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.utils.ActivityUtils;

import java.util.List;

import javax.inject.Inject;

import static android.widget.AdapterView.OnItemClickListener;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm route.
 */
public class SetRouteDialog extends AlarmModifierDialog {

    @Inject
    AlarmRouteSetter alarmRouteSetter;

    @Inject
    RoutesReceiver routesReceiver;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        List<Route> routes = this.routesReceiver.getRouteList();
        AutoComplete<Route> autoComplete = new AutoComplete<>(routes, this::onAutoCompleteItemSelected);
        autoComplete.attachAdapter(getActivity());

        AlarmRouteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_route, null, false);
        binding.setAlarm(getDraftAlarmWithRelations());
        binding.setAutocomplete(autoComplete);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.route_dialog_title)
                .setView(binding.getRoot())
                .setOnKeyListener(this::onKeyPressed)
                .create();

        ActivityUtils.showKeyboard(dialog.getWindow());
        return dialog;
    }

    void onAutoCompleteItemSelected(AdapterView adapterView, View view, int i, long l) {
        Route selectedRoute = (Route) adapterView.getItemAtPosition(i);
        if (!selectedRoute.equals(getDraftAlarmWithRelations().getRoute())) {
            this.alarmRouteSetter.onAlarmRouteSet(selectedRoute);
        }
        new android.os.Handler().postDelayed(this::dismiss, DELAY_DIALOG_DISMISS);
    }

    boolean onKeyPressed(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            dialogInterface.dismiss();
        }

        return true;
    }

    @BindingAdapter({"adapter", "validator", "onItemClick"})
    public static <T> void setupAutocomplete(AutoCompleteTextView autoComplete,
                                             ArrayAdapter<T> adapter,
                                             AutoCompleteTextView.Validator validator,
                                             OnItemClickListener onItemClick) {
        autoComplete.setAdapter(adapter);
        autoComplete.setValidator(validator);
        autoComplete.setOnItemClickListener(onItemClick);
    }
}

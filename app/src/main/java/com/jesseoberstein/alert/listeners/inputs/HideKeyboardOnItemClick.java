package com.jesseoberstein.alert.listeners.inputs;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jesseoberstein.alert.activities.alarm.CreateAlarm;

import static com.jesseoberstein.alert.utils.AlertUtils.hideKeyboardForLastInput;

/**
 * A listener that will hide the keyboard when an autocomplete item is selected.
 */
public class HideKeyboardOnItemClick implements AdapterView.OnItemClickListener {
    private Activity activity;

    public HideKeyboardOnItemClick(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CreateAlarm createAlarm = (CreateAlarm) activity;
        hideKeyboardForLastInput(createAlarm.getStationAutoComplete(), createAlarm.getFocusHolder());
        createAlarm.getDirectionsView().setVisibility(View.VISIBLE);
        createAlarm.setUpDirectionButtons(((TextView) view).getText().toString());
    }
}

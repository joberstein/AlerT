package com.jesseoberstein.alert.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jesseoberstein.alert.activities.alarm.CreateAlarm;

import java.util.function.Predicate;

import static com.jesseoberstein.alert.activities.alarm.CreateAlarm.NICKNAME_KEY;
import static com.jesseoberstein.alert.activities.alarm.CreateAlarm.STATION_KEY;

/**
 * Validates an editText input with the given predicate; watches for text changes via typing.
 */
public class CreateAlarmTextValidator extends AbstractValidator implements TextWatcher {
    private CreateAlarm activity;
    private int inputKey;

    public CreateAlarmTextValidator(CreateAlarm activity, Predicate<String> predicate,
                                    String errorMsg, Button submit, int inputKey) {
        super(predicate, errorMsg, submit, inputKey);
        EditText textToValidate = null;
        switch (inputKey) {
            case NICKNAME_KEY:
                textToValidate = activity.getNicknameText();
                break;
            case STATION_KEY:
                textToValidate = activity.getStationAutoComplete();
        }

        this.setInputTextView(textToValidate);
        this.activity = activity;
        this.inputKey = inputKey;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (this.inputKey == STATION_KEY) {
            activity.getDirectionsView().setVisibility(View.GONE);
            activity.getEndpointsView().setVisibility(View.GONE);
            activity.getLinesView().setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        validate(getPredicate().test(editable.toString().trim()));
    }
}

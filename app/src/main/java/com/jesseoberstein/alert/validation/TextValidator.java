package com.jesseoberstein.alert.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.function.Predicate;

/**
 * Validates an editText input with the given predicate; watches for text changes via typing.
 */
public class TextValidator extends AbstractValidator implements TextWatcher {

    public TextValidator(EditText editText, Predicate<String> predicate,
                         String errorMsg, Button submit, int inputKey) {
        super(editText, predicate, errorMsg, submit, inputKey);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        validate(getPredicate().test(editable.toString().trim()));
    }
}

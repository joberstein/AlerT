package com.jesseoberstein.alert.validation;

import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.function.Predicate;

/**
 * Validates an autocomplete input with the given predicate; watches autocomplete selection.
 */
public class AutoCompleteValidator extends AbstractValidator implements AutoCompleteTextView.Validator {

    public AutoCompleteValidator(AutoCompleteTextView autoComplete, Predicate<String> predicate,
                                 String errorMsg, Button submit, int inputKey) {
        super(autoComplete, predicate, errorMsg, submit, inputKey);
    }

    @Override
    public boolean isValid(CharSequence charSequence) {
        return validate(getPredicate().test(charSequence.toString().trim()));
    }

    @Override
    public CharSequence fixText(CharSequence charSequence) {
        return null;
    }
}

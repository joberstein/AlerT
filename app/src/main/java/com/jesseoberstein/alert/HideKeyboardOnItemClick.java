package com.jesseoberstein.alert;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.jesseoberstein.alert.utils.AlertUtils;

/**
 * A listener that will hide the keyboard when an autocomplete item is selected.
 */
public class HideKeyboardOnItemClick implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView autoCompleteView;
    private View nextFocus;

    public HideKeyboardOnItemClick(AutoCompleteTextView autoCompleteTextView, View nextFocus) {
        this.autoCompleteView = autoCompleteTextView;
        this.nextFocus = nextFocus;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertUtils.hideKeyboardForLastInput(this.autoCompleteView, this.nextFocus);
    }
}

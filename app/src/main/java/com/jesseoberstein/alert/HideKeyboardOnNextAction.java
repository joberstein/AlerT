package com.jesseoberstein.alert;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.jesseoberstein.alert.utils.AlertUtils;

import static android.view.inputmethod.EditorInfo.*;

/**
 * A listener that will hide the keyboard on a "next" click if this is the last input.
 */
public class HideKeyboardOnNextAction implements TextView.OnEditorActionListener {
    private View nextFocus;

    public HideKeyboardOnNextAction(View nextFocus) {
        this.nextFocus = nextFocus;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case IME_ACTION_NEXT:
                AlertUtils.hideKeyboardForLastInput(v, this.nextFocus);
        }
        return true;
    }

}

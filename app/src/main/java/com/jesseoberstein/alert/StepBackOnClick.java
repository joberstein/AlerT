package com.jesseoberstein.alert;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * A listener for a mobile stepper; decrements the page number on back click and updates the view.
 */
public class StepBackOnClick extends StepOnClick implements View.OnClickListener {

    public StepBackOnClick(View page, TextView stepText, View previous, View next, List<ImageView> steps) {
        super(page, stepText, previous, next, steps);
    }

    @Override
    public void onClick(View view) {
        this.showPreviousPage();
    }
}

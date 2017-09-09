package com.jesseoberstein.alert.listeners;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.jesseoberstein.alert.R;

/**
 * A listener that will toggle a Checkable's color and text color on click.
 */
public class ToggleColorOnClick implements OnCheckedChangeListener, OnItemClickListener, View.OnClickListener {

    private int activeColor;
    private int activeTextColor;
    private int inactiveColor;
    private int inactiveTextColor;
    private Context context;
    private boolean isActive; // for normal buttons.

    public ToggleColorOnClick(int activeColor, int activeTextColor, Context context) {
        this.activeColor = activeColor;
        this.activeTextColor = activeTextColor;
        this.context = context;
        this.inactiveColor = R.color.white;
        this.inactiveTextColor = R.color.gray;
        this.isActive = false;
    }

    public ToggleColorOnClick(int activeColor, int activeTextColor,
                              int inactiveColor, int inactiveTextColor, Context context) {
        this(activeColor, activeTextColor, context);
        this.inactiveColor = inactiveColor;
        this.inactiveTextColor = inactiveTextColor;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        this.isActive = isChecked;
        this.toggleButtonColorsOnClick(compoundButton);
    }

    @Override
    public void onClick(View view) {
        this.isActive = !this.isActive;
        this.toggleButtonColorsOnClick(((Button) view));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.toggleCheckableView(((CheckedTextView) view));
    }

    public void toggleButtonColorsOnClick(Button view) {
        int textColor = isActive ? this.activeTextColor : this.inactiveTextColor;
        view.setTextColor(this.context.getColor(textColor));

        int btnColor = isActive ? this.activeColor : this.inactiveColor;
        view.setBackgroundTintList(this.context.getColorStateList(btnColor));
    }

    private void toggleCheckableView(CheckedTextView view) {
        view.toggle();
        int textColor = view.isChecked() ? this.activeTextColor : this.inactiveTextColor;
        view.setTextColor(this.context.getColor(textColor));
        view.setBackground(createBackgroundStateList());
    }

    private StateListDrawable createBackgroundStateList() {
        StateListDrawable backgroundStateList = new StateListDrawable();

        backgroundStateList.addState(new int[]{android.R.attr.state_checked},
                new ColorDrawable(this.context.getColor(this.activeColor)));

        backgroundStateList.addState(StateSet.WILD_CARD,
                new ColorDrawable(this.context.getColor(this.inactiveColor)));

        return backgroundStateList;
    }
}

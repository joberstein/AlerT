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
public class ToggleColorOnClick implements OnCheckedChangeListener, OnItemClickListener {

    private int activeColor;
    private int activeTextColor;
    private int inactiveColor;
    private int inactiveTextColor;
    private Context context;

    public ToggleColorOnClick(int activeColor, int activeTextColor, Context context) {
        this.activeColor = activeColor;
        this.activeTextColor = activeTextColor;
        this.context = context;
        this.inactiveColor = R.color.white;
        this.inactiveTextColor = R.color.gray;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        this.toggleButtonColorsOnClick(compoundButton, isChecked);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.toggleListItemColorsOnClick(((CheckedTextView) view));
    }

    private void toggleButtonColorsOnClick(Button view, boolean isActive) {
        int textColor = isActive ? this.activeTextColor : this.inactiveTextColor;
        view.setTextColor(this.context.getColor(textColor));

        int btnColor = isActive ? this.activeColor : this.inactiveColor;
        view.setBackgroundTintList(this.context.getColorStateList(btnColor));
    }

    private void toggleListItemColorsOnClick(CheckedTextView view) {
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

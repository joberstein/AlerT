package com.jesseoberstein.alert.listeners;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Checkable;
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

    public ToggleColorOnClick() { }

    public ToggleColorOnClick(int activeColor, int activeTextColor, Context context) {
        this.activeColor = activeColor;
        this.activeTextColor = activeTextColor;
        this.context = context;
        this.inactiveColor = R.color.white;
        this.inactiveTextColor = R.color.gray;
    }

    private void toggleColorsOnClick(View view, boolean isActive) {
        Button btn = (Button) view;

        int textColor = isActive ? this.activeTextColor : this.inactiveTextColor;
        btn.setTextColor(this.context.getColor(textColor));

        int btnColor = isActive ? this.activeColor : this.inactiveColor;
        btn.setBackgroundTintList(ContextCompat.getColorStateList(this.context, btnColor));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        this.toggleColorsOnClick(compoundButton, isChecked);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((Checkable) view).toggle();
    }
}

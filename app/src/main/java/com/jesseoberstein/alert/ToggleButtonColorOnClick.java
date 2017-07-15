package com.jesseoberstein.alert;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

/**
 * A listener that will toggle a button's color and text color on click.
 */
public class ToggleButtonColorOnClick implements View.OnClickListener {
    private int activeColor;
    private int activeTextColor;
    private int inactiveColor;
    private int inactiveTextColor;
    private boolean isActive;
    private Context context;

    public ToggleButtonColorOnClick(int activeColor, int activeTextColor, Context context) {
        this.activeColor = activeColor;
        this.activeTextColor = activeTextColor;
        this.context = context;
        this.inactiveColor = R.color.white;
        this.inactiveTextColor = R.color.gray;
        this.isActive = false;
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        this.isActive = !this.isActive;

        int textColor = this.isActive ? this.activeTextColor : this.inactiveTextColor;
        btn.setTextColor(this.context.getColor(textColor));

        int btnColor = this.isActive ? this.activeColor : this.inactiveColor;
        btn.setBackgroundTintList(ContextCompat.getColorStateList(this.context, btnColor));
    }
}

package com.jesseoberstein.alert.listeners.alarm;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * A listener for a mobile stepper; decrements the current page in the given pager on back click.
 */
public class DecrementViewOnClick implements View.OnClickListener {

    private ViewPager pager;

    public DecrementViewOnClick(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public void onClick(View view) {
        int current = this.pager.getCurrentItem();
        int newItem = Math.max(0, current - 1);
        this.pager.setCurrentItem(newItem);
    }
}
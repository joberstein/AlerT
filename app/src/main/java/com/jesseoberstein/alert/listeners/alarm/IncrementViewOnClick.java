package com.jesseoberstein.alert.listeners.alarm;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * A listener for a mobile stepper; increments the current page in the given pager on next click.
 */
public class IncrementViewOnClick implements View.OnClickListener {
    private ViewPager pager;

    public IncrementViewOnClick(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public void onClick(View view) {
        int max = this.pager.getAdapter().getCount();
        int current = this.pager.getCurrentItem();
        int newItem = Math.min(max, current + 1);
        this.pager.setCurrentItem(newItem);
    }
}

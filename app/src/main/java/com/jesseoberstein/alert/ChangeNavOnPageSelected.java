package com.jesseoberstein.alert;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Responsible for handling changes to the navigation when a new page is selected.
 */
public class ChangeNavOnPageSelected implements ViewPager.OnPageChangeListener {
    private View prev;
    private View next;
    private View submit;

    public ChangeNavOnPageSelected(View prev, View next, View submit) {
        this.prev = prev;
        this.next = next;
        this.submit = submit;
    }

    /**
     * If on the first page, hide the back button. If on the last page, replace the next button with
     * the submit button. Update the step text for each page.
     * @param position The page number.
     */
    @Override
    public void onPageSelected(int position) {
        this.prev.setVisibility(VISIBLE);
        this.next.setVisibility(VISIBLE);
        this.submit.setVisibility(INVISIBLE);

        switch (position) {
            case 0:
                this.prev.setVisibility(INVISIBLE);
                break;
            case 2:
                this.next.setVisibility(INVISIBLE);
                this.submit.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageScrollStateChanged(int state) {}
}

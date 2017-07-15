package com.jesseoberstein.alert;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

/**
 * An abstract class implementing the logic for backward and forward stepping through pages.
 * A page number is set as a tag on the root element of the page. It should be initially set to "0",
 * and the initial page state should be set separately from this class.
 */
abstract class StepOnClick implements OnClickListener {
    private final String INCREMENT = "INCREMENT";
    private final String DECREMENT = "DECREMENT";

    private View page;
    private TextView stepText;
    private View previous;
    private View next;
    private List<ImageView> steps;
    private View finalView;

    StepOnClick(View page, TextView stepText, View previous, View next, List<ImageView> steps) {
        this.page = page;
        this.stepText = stepText;
        this.previous = previous;
        this.next = next;
        this.steps = steps;
        this.finalView = page.findViewById(R.id.final_step);
    }

    /**
     * Update the page number and show the next page view.
     */
    void showNextPage() {
        int newPageNumber = this.updatePageNumber(INCREMENT);
        this.showPage(newPageNumber);
    }

    /**
     * Update the page number and show the previous page view.
     */
    void showPreviousPage() {
        int newPageNumber = this.updatePageNumber(DECREMENT);
        this.showPage(newPageNumber);
    }

    /**
     * Show a page based on the given page number.
     * @param pageNumber The page number to show the view for.
     */
    private void showPage(int pageNumber) {
        this.steps.forEach(step -> step.setImageResource(R.drawable.circle_white));
        this.steps.get(pageNumber).setImageResource(R.drawable.circle_black);

        switch (pageNumber) {
            case 0:
                this.stepText.setText(R.string.step_1);
                this.previous.setVisibility(INVISIBLE);
                this.finalView.setVisibility(INVISIBLE);
                break;
            case 1:
                this.stepText.setText(R.string.step_2);
                this.next.setVisibility(VISIBLE);
                this.previous.setVisibility(VISIBLE);
                this.finalView.setVisibility(INVISIBLE);
                this.steps.get(pageNumber).setImageResource(R.drawable.circle_black);
                break;
            case 2:
                this.stepText.setText(R.string.step_3);
                this.next.setVisibility(INVISIBLE);
                this.finalView.setVisibility(VISIBLE);
                this.steps.get(pageNumber).setImageResource(R.drawable.circle_black);
        }
    }

    /**
     * Update the page number according to INCREMENT|DECREMENT. If neither, the page number should
     * remain the same.
     * @param action Should be one of INCREMENT|DECREMENT
     * @return The updated page number.
     */
    private int updatePageNumber(String action) {
        int pageNumber = Integer.parseInt((String) this.page.getTag());
        switch (action) {
            case INCREMENT:
                pageNumber++;
                break;
            case DECREMENT:
                pageNumber--;
                break;
        }

        this.page.setTag(Integer.valueOf(pageNumber).toString());
        return pageNumber;
    }
}

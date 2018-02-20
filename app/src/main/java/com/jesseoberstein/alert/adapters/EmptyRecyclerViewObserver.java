package com.jesseoberstein.alert.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Borrowed from https://gist.github.com/sheharyarn/5602930ad84fa64c30a29ab18eb69c6e
 */
public class EmptyRecyclerViewObserver extends RecyclerView.AdapterDataObserver {
    private RecyclerView recyclerView;
    private View emptyView;

    public EmptyRecyclerViewObserver(RecyclerView recyclerView, View emptyView) {
        this.recyclerView = recyclerView;
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    @Override
    public void onChanged() {
        super.onChanged();
        checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (emptyView != null && recyclerView.getAdapter() != null) {
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }
}

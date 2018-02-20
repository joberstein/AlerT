package com.jesseoberstein.alert.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder> {
    private int layout;
    private List<T> items;

    BaseRecyclerAdapter(int layout, List<T> items) {
        this.layout = layout;
        this.items = items;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(this.getInflatedView(parent));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    View getInflatedView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    }

    List<T> getItems() {
        return this.items;
    }

    public void addItem(T item) {
        this.items.add(item);
        if (getItemCount() == 1) {
            this.notifyDataSetChanged();
        } else {
            this.notifyItemInserted(this.items.size() - 1);
        }
    }

    public void removeItem(T itemToRemove) {
        int idx = this.items.indexOf(itemToRemove);
        this.items.remove(itemToRemove);
        this.notifyItemRemoved(idx);
        this.notifyItemRangeChanged(idx, getItemCount() - idx);
    }

    public void removeItem(int indexToRemove) {
        this.items.remove(indexToRemove);
        this.notifyItemRemoved(indexToRemove);
        this.notifyItemRangeChanged(indexToRemove, getItemCount() - indexToRemove);
    }

    public void updateItem(T itemToUpdate) {
        int itemPosition = this.items.indexOf(itemToUpdate);
        this.items.set(itemPosition, itemToUpdate);
        this.notifyItemChanged(itemPosition, itemToUpdate);
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View view) {
            super(view);
        }
    }
}
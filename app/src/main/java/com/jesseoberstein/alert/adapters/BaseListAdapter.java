package com.jesseoberstein.alert.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Optional;

public class BaseListAdapter<T> extends ArrayAdapter<T> {
    private final LayoutInflater inflater;
    private int view;
    private ArrayList<T> items = new ArrayList<>();

    BaseListAdapter(Context context, int view, ArrayList<T> itemList) {
        super(context, view, itemList);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = view;
        this.items = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent, true);
    }

    public void setView(int view) {
        this.view = view;
    }

    // Inflate a new view or use the convert view if it exists, and set a given item's properties on
    // the view.
    View getView(int position, View convertView, @NonNull ViewGroup parent, boolean setViewProps) {
        View newView = this.inflater.inflate(this.view, parent, false);
        View view = (convertView == null) ? newView : convertView;

        if (setViewProps) {
            T item = this.items.get(position);
            setItemPropertiesInView(item, view, position);
        }

        return view;
    }

    public ArrayList<T> getItems() {
        return new ArrayList<>(this.items);
    }

    public void addItem(T item) {
        this.items.add(item);
        this.notifyDataSetChanged();
    }

    void removeItem(T itemToRemove) {
        this.items.remove(itemToRemove);
        this.notifyDataSetChanged();
    }

    void updateItem(T itemToUpdate) {
        int itemPosition = this.items.indexOf(itemToUpdate);
        this.items.set(itemPosition, itemToUpdate);
        this.notifyDataSetChanged();
    }

    // Should override this in implementing class.
    void setItemPropertiesInView(T item, View v, int position) {}

    Optional<View> getViewById(int id, View parentView) {
        return Optional.ofNullable(parentView.findViewById(id));
    }
}
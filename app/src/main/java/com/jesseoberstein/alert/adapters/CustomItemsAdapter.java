package com.jesseoberstein.alert.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Optional;

public class CustomItemsAdapter extends BaseListAdapter<CustomListItem> {

    private final int LIST_DIVIDER = 0;
    private final int CUSTOM_LIST_ITEM = 1;

    public CustomItemsAdapter(Context context, int view, ArrayList<CustomListItem> items) {
        super(context, view, items);
    }

    public void removeItemByPrimaryText(String primaryText) {
        getItems().stream()
                .filter(item -> item.getPrimaryText().equals(primaryText))
                .findAny()
                .ifPresent(this::removeItem);
    }

    public void removeItemById(int id) {
        getItems().stream()
                .filter(item -> item.getId().equals(id))
                .findAny()
                .ifPresent(this::removeItem);
    }

    public void updateItemById(CustomListItem itemToUpdate) {
        getItems().stream()
                .filter(item -> item.getId() == itemToUpdate.getId())
                .findAny()
                .ifPresent(this::updateItem);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        CustomListItem item = getItem(position);
        return (item == null || item.isDivider()) ? LIST_DIVIDER : CUSTOM_LIST_ITEM;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        boolean isDivider = (getItemViewType(position) == LIST_DIVIDER);
        return getView(position, convertView, parent, !isDivider);
    }

    @Override
    void setItemPropertiesInView(CustomListItem item, View view, int position) {
        getViewById(R.id.custom_item_primary_text, view).ifPresent(v -> ((TextView) v).setText(item.getPrimaryText()));
        getViewById(R.id.custom_item_secondary_text, view).ifPresent(v -> ((TextView) v).setText(item.getSecondaryText()));
        getViewById(R.id.custom_item_tertiary_text, view).ifPresent(v -> ((TextView) v).setText(item.getTertiaryText()));
        getViewById(R.id.custom_item_info_text, view).ifPresent(v -> ((TextView) v).setText(item.getInfo()));
        getViewById(R.id.custom_item_icon, view).ifPresent(v -> ((ImageView) v).setImageResource(item.getIcon()));
        getViewById(R.id.custom_item_chevron, view).ifPresent(v -> ((ImageView) v).setImageResource(item.getChevron()));

        // Set the info view as visible if info is present.
        Optional.ofNullable(item.getInfo()).ifPresent(val -> {
            getViewById(R.id.custom_item_info, view).ifPresent(v -> v.setVisibility(View.VISIBLE));
        });
    }
}
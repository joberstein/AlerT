package com.jesseoberstein.alert.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.utils.ListProp;
import java.util.ArrayList;
import java.util.Optional;
import static com.jesseoberstein.alert.utils.Constants.CUSTOM_LIST_IDS;
import static com.jesseoberstein.alert.utils.Constants.CUSTOM_LIST_PROPS;
import static com.jesseoberstein.alert.utils.AlertUtils.listsToMap;

public class CustomListAdapter extends ArrayAdapter<CustomListItem> {
    private final LayoutInflater inflater;
    private final int view;
    private ArrayList<CustomListItem> items = new ArrayList<>();

    private final int LIST_DIVIDER = 0;
    private final int CUSTOM_LIST_ITEM = 1;
    private final int NUM_VIEWTYPES = 2;

    public CustomListAdapter(Context context, int view, ArrayList<CustomListItem> itemList) {
        super(context, view, itemList);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = view;
        this.items = itemList;
    }

    public ArrayList<CustomListItem> getItems() {
        return new ArrayList<>(this.items);
    }

    public void addItem(CustomListItem item) {
        this.items.add(item);
        this.notifyDataSetChanged();
    }

    public void removeItemByPrimaryText(String primaryText) {
        ArrayList<CustomListItem> listItems = new ArrayList<>(this.items);
        listItems.stream()
                .filter(item -> item.getPrimaryText().equals(primaryText))
                .findAny()
                .ifPresent(itemToRemove -> this.items.remove(itemToRemove));
        this.notifyDataSetChanged();
    }

    public void removeItemById(int id) {
        ArrayList<CustomListItem> listItems = new ArrayList<>(this.items);
        listItems.stream()
                .filter(item -> item.getId() == id)
                .findAny()
                .ifPresent(itemToRemove -> this.items.remove(itemToRemove));
        this.notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return NUM_VIEWTYPES;
    }

    @Override
    public int getItemViewType(int position) {
        Optional<CustomListItem> optItem = Optional.ofNullable(getItem(position));
        return optItem.map(CustomListItem::isDivider).orElse(true) ? LIST_DIVIDER : CUSTOM_LIST_ITEM;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        int viewToInflate = (getItemViewType(position) == LIST_DIVIDER) ? R.layout.list_divider : this.view;
        final View rowToDisplay = this.inflater.inflate(viewToInflate, parent, false);

        switch (getItemViewType(position)) {
            case CUSTOM_LIST_ITEM:
                CustomListItem item = this.items.get(position);
                listsToMap(CUSTOM_LIST_IDS, CUSTOM_LIST_PROPS).forEach((key, value) ->
                        setItemViewValue(item, value, rowToDisplay.findViewById(key)));
            default:
                return rowToDisplay;
        }
    }

    /**
     * Set the item view for the given property.
     * @param item The item to get the property from.
     * @param propKey The property to set the view for.
     * @param v The view to set the property on.
     * @param <T> A string (text view value) or int (image view resource id).
     */
    private <T> void setItemViewValue(CustomListItem item, ListProp propKey, View v) {
        Optional<View> optView = Optional.ofNullable(v);
        T propValue = getItemPropertyValue(item, propKey);
        switch (propKey) {
            case INFO:
                optView.ifPresent(view -> Optional.ofNullable(propValue).ifPresent(val -> view.setVisibility(View.VISIBLE)));
                optView = optView.map(view -> view.findViewById(R.id.custom_item_info_text));
            case PRIMARY_TEXT:
            case SECONDARY_TEXT:
            case TERTIARY_TEXT:
                optView.ifPresent(view -> ((TextView) view).setText((String) propValue));
                break;
            case ICON:
            case CHEVRON:
                optView.ifPresent(view -> ((ImageView) view).setImageResource((Integer) propValue));
        }
    }

    /**
     * Get an item property based on the given key.
     * @param item The item to get properties from.
     * @param propKey One of ListProp, a key representing an item's property.
     * @param <T> The item property, a String or int (id).
     * @return An item property.
     */
    private <T> T getItemPropertyValue(CustomListItem item, ListProp propKey) {
        switch (propKey) {
            case ICON:              return (T) Integer.valueOf(item.getIcon());
            case PRIMARY_TEXT:      return (T) item.getPrimaryText();
            case SECONDARY_TEXT:    return (T) item.getSecondaryText();
            case TERTIARY_TEXT:     return (T) item.getTertiaryText();
            case INFO:              return (T) item.getInfo();
            case CHEVRON:           return (T) Integer.valueOf(item.getChevron());
            default:                return (T) "";
        }
    }
}
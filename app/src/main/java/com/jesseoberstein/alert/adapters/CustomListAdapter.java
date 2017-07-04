package com.jesseoberstein.alert.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.utils.AlertUtils;
import java.util.*;

public class CustomListAdapter extends ArrayAdapter<CustomListItem> {

    private static final String ICON = "icon";
    private static final String NAME = "name";
    private static final String INFO = "info";
    private static final String CHEVRON = "chevron";

    private final Context context;
    private final ArrayList<CustomListItem> modelsArrayList;

    public CustomListAdapter(Context context, ArrayList<CustomListItem> itemList) {
        super(context, R.layout.route_list_row, itemList);
        this.context = context;
        this.modelsArrayList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.route_list_row, parent, false);
        ImageView iconView = (ImageView) rowView.findViewById(R.id.custom_item_icon);
        TextView nameView = (TextView) rowView.findViewById(R.id.custom_item_title);
        View infoView = rowView.findViewById(R.id.custom_item_info);
        TextView infoTextView = (TextView) rowView.findViewById(R.id.custom_item_info_text);
        ImageView chevronView = (ImageView) rowView.findViewById(R.id.custom_item_chevron);

        CustomListItem item = modelsArrayList.get(position);

        iconView.setImageResource(item.getIcon());
        nameView.setText(item.getName());
        infoTextView.setText(item.getInfo());
        chevronView.setImageResource(item.getChevron());

        List<String> props = Arrays.asList(ICON, NAME, INFO, CHEVRON);
        List<View> views = Arrays.asList(iconView, nameView, infoView, chevronView);
        Map<String, View> viewsMap = AlertUtils.listsToMap(props, views);
        showViewsIfPropertiesExist(item, viewsMap);

        return rowView;
    }

    private <T> void showViewsIfPropertiesExist(CustomListItem item, Map<String, View> viewsMap) {
        viewsMap.forEach((propKey, view) -> {
            Optional<T> property = Optional.ofNullable(getCustomListItemProp(item, propKey));
            property.ifPresent(prop -> view.setVisibility(View.VISIBLE));
        });
    }

    private <T> T getCustomListItemProp(CustomListItem item, String propKey) {
        switch (propKey) {
            case ICON:    return (T) Integer.valueOf(item.getIcon());
            case NAME:    return (T) item.getName();
            case INFO:    return (T) item.getInfo();
            case CHEVRON: return (T) Integer.valueOf(item.getChevron());
            default:      return (T) "";
        }
    }
}
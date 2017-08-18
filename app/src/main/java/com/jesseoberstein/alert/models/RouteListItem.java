package com.jesseoberstein.alert.models;

import com.jesseoberstein.alert.R;

/**
 * A wrapper class for building a custom list item; targeted towards list of routes.
 */
public class RouteListItem extends CustomListItem {

    /**
     * Build a route list item with the given list item.
     */
    public static CustomListItem buildRoutesListItem(int icon, String name, String alerts) {
        String info = (!alerts.isEmpty() && Integer.parseInt(alerts) > 0) ? alerts : null;
        return new CustomListItem()
                .withPrimaryText(name)
                .withIcon(icon)
                .withInfo(info)
                .withChevron(R.drawable.ic_chevron_right);
    }
}

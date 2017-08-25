package com.jesseoberstein.alert.models;

import com.jesseoberstein.alert.R;

/**
 * A wrapper class for building a custom list item; targeted towards list of routes.
 */
public class RouteListItem extends CustomListItem {

    /**
     * Build a route list item with the given user route.
     */
    public static CustomListItem buildRoutesListItem(UserRoute userRoute) {
        userRoute.setRouteResources();
        String alerts = userRoute.getAlerts();
        String info = (!alerts.isEmpty() && Integer.parseInt(alerts) > 0) ? alerts : null;

        return new CustomListItem()
                .withPrimaryText(userRoute.getRouteName())
                .withIcon(userRoute.getIcon())
                .withInfo(info)
                .withChevron(R.drawable.ic_chevron_right);
    }
}

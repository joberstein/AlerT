package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.RouteType;

import java.util.Optional;

import static com.jesseoberstein.alert.models.mbta.RouteType.BOAT;
import static com.jesseoberstein.alert.models.mbta.RouteType.BUS;
import static com.jesseoberstein.alert.models.mbta.RouteType.COMMUTER_RAIL;

public class AlertUtils {

    /**
     * Given a route, get it's theme based on the route type and then route id if necessary.
     * @param route A Route to get the theme for.
     * @return The theme for the given route. Defaults to a dark gray theme.
     */
    public static int getTheme(Route route) {
        String routeId = Optional.ofNullable(route).map(Route::getId).orElse("");

        if (routeId.matches("Green-\\w{1}")) {
            return R.style.AlarmSettingsDark_Green;
        }

        switch (routeId) {
            case "Blue":
                return R.style.AlarmSettingsDark_Blue;
            case "Orange":
                return R.style.AlarmSettingsDark_Orange;
            case "Red":
            case "Mattapan":
                return R.style.AlarmSettingsDark_Red;
            case "741": // SL1
            case "742": // SL2
            case "743": // SL3
            case "746": // SL Waterfront
            case "749": // SL5
            case "751": // SL4
                return R.style.AlarmSettingsDark_Silver;
            default:
                return getThemeByRouteType(route);
        }
    }

    /**
     * Given a route id, get the theme by route type.
     * @param route A Route to get the theme for.
     * @return The theme for the given route id. Defaults to a dark gray theme.
     */
    private static int getThemeByRouteType(Route route) {
        int defaultTheme = R.style.AlarmSettingsDark_Default;
        if (route == null) {
            return defaultTheme;
        }

        RouteType routeType = RouteType.valueOf(route.getRouteTypeId());
        if (BOAT.equals(routeType)) {
            return R.style.AlarmSettingsDark_Boat;
        } else if (BUS.equals(routeType)) {
            return R.style.AlarmSettingsLight_Bus;
        } else if (COMMUTER_RAIL.equals(routeType)) {
            return R.style.AlarmSettingsDark_Commuter;
        } else {
            return defaultTheme;
        }
    }

    /**
     * Get the text color for when the background color is white; i.e. if the text color is already
     * white, use the route color.
     * @param route containing the a color and text color.
     * @return The text color to use for a white background.
     */
    public static String getTextColorForWhiteBackground(Route route) {
        String routeColor = route.getColor();
        String routeTextColor = route.getTextColor();
        return "FFFFFF".equals(routeTextColor) ? routeColor : routeTextColor;
    }

    public static String getHexColor(String color) {
        return Optional.ofNullable(color).map(c -> "#" + c).orElse("");
    }
}

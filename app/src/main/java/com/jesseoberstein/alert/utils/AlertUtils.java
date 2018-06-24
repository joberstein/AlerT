package com.jesseoberstein.alert.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.RouteType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.models.mbta.RouteType.*;

public class AlertUtils {

    /**
     * Create a map with two given lists.  The two lists should be equal in size.
     * @param keys A list of keys for the resulting map.
     * @param values A list of values for the resulting map.
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     * @return A map of K keys to V values.
     */
    public static <K, V> Map<K, V> listsToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

    /**
     * Is the given number even?
     * @param i The number to check.
     * @return True if even, false otherwise.
     */
    public static boolean isEven(int i) {
        return i % 2 == 0;
    }

    /**
     * Show the keyboard for the given view.
     * @param window The window to show the keyboard for.
     */
    public static void showKeyboard(Window window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * Hide the keyboard if the given next focus is a dummy focus holder (0 width).
     * @param input The potential last input.
     * @param nextFocus The next view to focus on.
     */
    public static void hideKeyboardForLastInput(View input, View nextFocus) {
        input.clearFocus();
        if (nextFocus.getWidth() == 0) {
            AlertUtils.hideKeyboard(nextFocus);
        }
    }

    /**
     * Hide the keyboard for the given view.
     * @param view The view to hide the keyboard for.
     */
    private static void hideKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Add an extra to an existing bundle, agnostic of a given cursor item's type.
     * @param bundle An existing bundle
     * @param columnIndex The index of the column to get the name and type of.
     * @param cursor The cursor, should be positioned at an element.
     */
    public static void putExtraByField(Bundle bundle, Cursor cursor, int columnIndex) {
        String columnName = cursor.getColumnName(columnIndex);
        switch (cursor.getType(columnIndex)) {
            case 1:
                bundle.putInt(columnName, cursor.getInt(columnIndex));
                break;
            case 2:
                bundle.putFloat(columnName, cursor.getFloat(columnIndex));
                break;
            case 3:
                bundle.putString(columnName, cursor.getString(columnIndex));
                break;
            case 4:
                bundle.putByteArray(columnName, cursor.getBlob(columnIndex));
                break;
        }
    }

    public static Optional<Object> getObjectFromIntent(@Nullable Intent intent, String key) {
        return Optional.ofNullable(intent)
                .map(i -> Optional.ofNullable(i.getExtras().get(key)))
                .orElse(Optional.empty());
    }

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
     * @param alarm containing the route color and text color.
     * @return The text color to use for a white background.
     */
    public static String getTextColorForWhiteBackground(UserAlarm alarm) {
        String routeColor = alarm.getRoute().getColor();
        String routeTextColor = alarm.getRoute().getTextColor();
        return "FFFFFF".equals(routeTextColor) ? routeColor : routeTextColor;
    }

    public static String getHexColor(String color) {
        return Optional.ofNullable(color).map(c -> "#" + c).orElse("");
    }
}

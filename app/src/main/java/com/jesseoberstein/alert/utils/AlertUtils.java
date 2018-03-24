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

import com.jesseoberstein.alert.models.UserRoute;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.DEFAULT_ROUTE;
import static com.jesseoberstein.alert.utils.Constants.ICON;
import static com.jesseoberstein.alert.utils.Constants.THEME;
import static com.jesseoberstein.alert.utils.Constants.USER_ROUTES;

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

    /**
     * Get the icon for the corresponding route; if a route cannot be found, default to a black circle.
     * @param routeName The name of the route to get the icon for.
     * @return A drawable representing a route icon.
     */
    public static int getRouteResource(String routeName, String resourceType) {
        UserRoute userRoute = USER_ROUTES.stream()
                .filter(route -> route.getRouteName().equals(routeName))
                .findFirst().orElseGet(() -> DEFAULT_ROUTE);

        switch (resourceType) {
            case ICON:    return userRoute.getIcon();
            case COLOR:   return userRoute.getColor();
            case THEME:   return userRoute.getTheme();
            default:        return -1;
        }
    }

    public static Optional<Object> getObjectFromIntent(@Nullable Intent intent, String key) {
        return Optional.ofNullable(intent)
                .map(i -> Optional.ofNullable(i.getExtras().get(key)))
                .orElse(Optional.empty());
    }
}

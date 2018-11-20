package com.jesseoberstein.alert.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtils {

    /**
     * Set the given menu icon's color with the given color.
     * @param context The activity's context.
     * @param icon The icon to recolor.
     * @param attrColor The color to restyle the icon with, specified as an attribute (likely from a theme).
     */
    public static void setDrawableColor(Context context, Drawable icon, int attrColor) {
        icon.setColorFilter(getAttrValue(context, attrColor), PorterDuff.Mode.SRC_IN);
    }

    /**
     * Get an attribute's value.
     * @param context The activity's context.
     * @param attr A reference to the attribute.
     * @return The value of the given attribute.
     */
    private static int getAttrValue(Context context, int attr) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, outValue, true);
        return outValue.data;
    }

    /**
     * Show the keyboard for the given view.
     * @param window The window to show the keyboard for.
     */
    public static void showKeyboard(Window window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}

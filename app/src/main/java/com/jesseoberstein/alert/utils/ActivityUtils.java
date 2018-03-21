package com.jesseoberstein.alert.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.jesseoberstein.alert.R;

public class ActivityUtils {

    /**
     * Set the given icon's color with the given color.
     * @param context The activity's context.
     * @param icon The icon to recolor.
     * @param color The color to restyle the icon with.
     */
    public static void setIconColor(Context context, Drawable icon, int color) {
        icon.setColorFilter(getAttrValue(context, color), PorterDuff.Mode.SRC_IN);
    }

    /**
     * Get an attribute's value.
     * @param context The activity's context.
     * @param attr A reference to the attribute.
     * @return The value of the given attribute.
     */
    public static int getAttrValue(Context context, int attr) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, outValue, true);
        return outValue.data;
    }

    /**
     * Set the text for a section label.
     * @param section The section whose label to set text for.
     * @param label The text to set as the section's label.
     */
    public static void setSectionLabelText(View section, String label) {
        TextView sectionLabel = (TextView) section.findViewById(R.id.alarmSettings_section_label);
        sectionLabel.setText(label);
    }

    /**
     * Set the text for a section value.
     * @param section The section whose value to set text for.
     * @param value The text to set as the section's value.
     * @param size The font size of the section's value text.
     */
    public static void setSectionValueText(View section, String value, int size) {
        TextView sectionValue = (TextView) section.findViewById(R.id.alarmSettings_section_value);
        sectionValue.setText(value);
        sectionValue.setTextSize(size);
        sectionValue.setTextIsSelectable(false);
    }
}

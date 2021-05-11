package com.jesseoberstein.alert.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

import com.jesseoberstein.alert.R;

import java.util.stream.IntStream;

/**
 * Utility class for creating background tint {@link ColorStateList}s.
 * Base code sourced from:
 * @see <a href="http://www.androiddesignpatterns.com/2016/08/coloring-buttons-with-themeoverlays-background-tints.html" />
 */
public final class Tints {
    private static final int[] DISABLED_STATE_SET = new int[]{-android.R.attr.state_enabled};
    private static final int[] EMPTY_STATE_SET = new int[0];

    /**
     * Returns a {@link ColorStateList} that can be used as a colored button's background tint.
     * Note that this code makes use of the {@code android.support.v4.graphics.ColorUtils}
     * utility class.
     */
    public static ColorStateList forColoredButtonText(Context context, @ColorInt int color) {
        return getColorStateList(color, context.getColor(R.color.medium_gray));
    }

    public static ColorStateList forColoredButton(Context context, @ColorInt int color) {
        return getColorStateList(color, getDisabledButtonColor(context));
    }

    private static ColorStateList getColorStateList(@ColorInt int color, @ColorInt int disabledColor) {
        final int numStates = 2;
        final int[][] states = new int[numStates][];
        final int[] colors = new int[numStates];

        IntStream.range(0, numStates).forEach(i -> {
            switch (i) {
                case 0:
                    states[i] = DISABLED_STATE_SET;
                    colors[i] = disabledColor;
                    break;
                case 1:
                    states[i] = EMPTY_STATE_SET;
                    colors[i] = color;
                    break;
            }
        });

        return new ColorStateList(states, colors);
    }

    /**
     * Returns the theme-dependent ARGB background color to use for disabled buttons.
     */
    @ColorInt
    private static int getDisabledButtonColor(Context context) {
        final float disabledAlpha = getDisabledAlpha(context) * 3 / 2;

        // Use the disabled alpha factor and the button's default normal color
        // to generate the button's disabled background color.
        final int colorButtonNormal = getThemeAttrColor(context, R.attr.colorButtonNormal);
        final int originalAlpha = Color.alpha(colorButtonNormal);
        return ColorUtils.setAlphaComponent(colorButtonNormal, Math.round(originalAlpha + disabledAlpha));
    }

    @ColorInt
    private static int getDisabledButtonTextColor(Context context) {
        final float disabledAlpha = getDisabledAlpha(context) / 2;

        // Use the disabled alpha factor and the button's default normal color
        // to generate the button's disabled background color.
        final int colorButtonNormal = getThemeAttrColor(context, R.attr.colorButtonNormal);
        final int originalAlpha = Color.alpha(colorButtonNormal);
        return ColorUtils.setAlphaComponent(colorButtonNormal, Math.round(originalAlpha + disabledAlpha));
    }

    private static float getDisabledAlpha(Context context) {
        // Extract the disabled alpha to apply to the button using the context's theme.
        // (0.26f for light themes and 0.30f for dark themes).
        final TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.disabledAlpha, tv, true);
        return tv.getFloat();
    }


    /** Returns the theme-dependent ARGB color associated with the provided theme attribute. */
    @ColorInt
    private static int getThemeAttrColor(Context context, @AttrRes int attr) {
        final TypedArray array = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return array.getColor(0, 0);
        } finally {
            array.recycle();
        }
    }

    private Tints() {}
}
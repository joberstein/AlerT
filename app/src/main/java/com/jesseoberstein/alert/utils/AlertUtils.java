package com.jesseoberstein.alert.utils;

import android.app.Activity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;

import com.jesseoberstein.alert.R;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.utils.ListProp.CHEVRON;
import static com.jesseoberstein.alert.utils.ListProp.ICON;
import static com.jesseoberstein.alert.utils.ListProp.INFO;
import static com.jesseoberstein.alert.utils.ListProp.PRIMARY_TEXT;
import static com.jesseoberstein.alert.utils.ListProp.SECONDARY_TEXT;
import static com.jesseoberstein.alert.utils.ListProp.TERTIARY_TEXT;

public class AlertUtils {

    public static List<ListProp> CUSTOM_LIST_PROPS = Arrays.asList(
            ICON, PRIMARY_TEXT, SECONDARY_TEXT, TERTIARY_TEXT, INFO, CHEVRON);

    public static List<Integer> CUSTOM_LIST_IDS = Arrays.asList(
            R.id.custom_item_icon, R.id.custom_item_primary_text, R.id.custom_item_secondary_text,
            R.id.custom_item_tertiary_text, R.id.custom_item_info, R.id.custom_item_chevron);

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
}

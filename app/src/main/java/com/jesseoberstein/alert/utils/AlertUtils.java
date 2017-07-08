package com.jesseoberstein.alert.utils;

import android.app.Activity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;

import com.jesseoberstein.alert.R;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AlertUtils {

    public static <K, V> Map<K, V> listsToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

    public static SimpleCursorAdapter newSimpleCursorAdapter(Activity activity, int layout,
                                                             String[] from, int[] to) {
        return new SimpleCursorAdapter(activity, layout, null, from, to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }
}

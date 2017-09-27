package com.jesseoberstein.alert.utils;

import android.content.Context;

import java.util.Calendar;

import static java.util.Calendar.getInstance;

public class DateUtils {

    public static long unixTimestampToMillis(Long ts) {
        return ts * 1000;
    }

    public static boolean isUnixTimestampAfterCurrentTime(Long ts) {
        Calendar now = getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        Calendar arrival = getInstance();
        arrival.setTimeInMillis(unixTimestampToMillis(ts));
        return arrival.after(now);
    }

    public static String formatUnixTimestamp(Context context, Long ts) {
        return android.text.format.DateUtils.formatDateTime(context, unixTimestampToMillis(ts), android.text.format.DateUtils.FORMAT_SHOW_TIME);
    }
}

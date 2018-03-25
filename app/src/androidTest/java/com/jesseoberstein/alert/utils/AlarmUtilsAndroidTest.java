package com.jesseoberstein.alert.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AlarmUtilsAndroidTest {
    private Context context;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void intentFilters() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        int requestCode = 1;
        int flag = 0;

        PendingIntent p1 = PendingIntent.getBroadcast(context, requestCode, intent, flag);

        requestCode = 2;
        PendingIntent p2 = PendingIntent.getBroadcast(context, requestCode, intent, flag);

        intent.setAction(Intent.ACTION_INSERT);
        PendingIntent p3 = PendingIntent.getBroadcast(context, requestCode, intent, flag);

        intent.setData(new Uri.Builder().scheme("scheme").path("path").build());
        PendingIntent p4 = PendingIntent.getBroadcast(context, requestCode,intent, flag);

        flag = 1;
        PendingIntent p5 = PendingIntent.getBroadcast(context, requestCode, intent, flag);

        intent.putExtra("extra", "extra");
        PendingIntent p6 = PendingIntent.getBroadcast(context, requestCode, intent, flag);

        assertNotEquals(p1, p2); // request code change
        assertNotEquals(p2, p3); // action change
        assertNotEquals(p3, p4); // data change
        assertNotEquals(p4, p5); // flag change
        assertEquals(p5, p6); // extras change
    }
}
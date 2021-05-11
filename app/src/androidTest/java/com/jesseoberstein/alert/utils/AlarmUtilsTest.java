package com.jesseoberstein.alert.utils;

import android.content.Context;
import android.net.Uri;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;

import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class AlarmUtilsTest {

    @Mock
    private UserAlarm testAlarm;

    @Mock
    private UserAlarmWithRelations testAlarmWithRelations;

    private Context context;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testCreatesAlarmEndpoints() {
        long alarmId = 1;
        long e1Id = 2;
        long e2Id = 3;
        Endpoint e1 = new Endpoint("Oak Grove", 0, "Orange");
        Endpoint e2 = new Endpoint("Forest Hills", 1, "Orange");

        e1.setId(e1Id);
        e2.setId(e2Id);

        when(testAlarm.getId()).thenReturn(alarmId);
        when(testAlarmWithRelations.getAlarm()).thenReturn(testAlarm);
        when(testAlarmWithRelations.getEndpoints()).thenReturn(Arrays.asList(e1, e2));

        AlarmEndpoint[] expected = new AlarmEndpoint[]{
                new AlarmEndpoint(alarmId, e1Id),
                new AlarmEndpoint(alarmId, e2Id)
        };

        assertTrue(Arrays.equals(expected, AlarmUtils.createAlarmEndpoints(testAlarmWithRelations)));
    }

    @Test
    public void testBuildUri() {
        assertEquals(Uri.parse("content:/alarms/1"), AlarmUtils.buildAlarmUri(1));
    }
}
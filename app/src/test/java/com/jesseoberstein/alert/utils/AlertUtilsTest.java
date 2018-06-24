package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.RouteType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlertUtilsTest {
    @Mock private UserAlarm testAlarm;
    @Mock private Route testRoute;

    @Test
    public void getThemeByRouteId() {
        Map<String, Integer> themes = new HashMap<>();
        themes.put("Blue", R.style.AlarmSettingsDark_Blue);
        themes.put("Green-B", R.style.AlarmSettingsDark_Green);
        themes.put("Mattapan", R.style.AlarmSettingsDark_Red);
        themes.put("Orange", R.style.AlarmSettingsDark_Orange);
        themes.put("Red", R.style.AlarmSettingsDark_Red);
        themes.put("751", R.style.AlarmSettingsDark_Silver);

        themes.forEach((key, value) -> {
            System.out.println("Get theme for route id: " + key);
            when(testRoute.getId()).thenReturn(key);
            assertEquals(value.intValue(), AlertUtils.getTheme(testRoute));
        });
    }

    @Test
    public void getThemeByRouteType() {
        Map<RouteType, Integer> themes = new HashMap<>();
        themes.put(RouteType.BOAT, R.style.AlarmSettingsDark_Boat);
        themes.put(RouteType.BUS, R.style.AlarmSettingsLight_Bus);
        themes.put(RouteType.COMMUTER_RAIL, R.style.AlarmSettingsDark_Commuter);

        themes.forEach((key, value) -> {
            System.out.println("Get theme for route type: " + key);
            when(testRoute.getRouteTypeId()).thenReturn(key.getId());
            assertEquals(value.intValue(), AlertUtils.getTheme(testRoute));
        });
    }

    @Test
    public void getsTextColorForWhiteBackground_textColorIsWhite() {
        when(testAlarm.getRoute()).thenReturn(testRoute);
        when(testRoute.getColor()).thenReturn("000000");
        when(testRoute.getTextColor()).thenReturn("FFFFFF");
        assertEquals("000000", AlertUtils.getTextColorForWhiteBackground(testAlarm));
    }

    @Test
    public void getsTextColorForWhiteBackground_textColorIsNotWhite() {
        when(testAlarm.getRoute()).thenReturn(testRoute);
        when(testRoute.getColor()).thenReturn("FFFFFF");
        when(testRoute.getTextColor()).thenReturn("000000");
        assertEquals("000000", AlertUtils.getTextColorForWhiteBackground(testAlarm));
    }
}
package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.RouteType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AlertUtilsTest {

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
            Route route = new Route();
            route.setId(key);

            System.out.println("Get theme for route id: " + key);
            assertEquals(value.intValue(), AlertUtils.getTheme(route));
        });
    }

    @Test
    public void getThemeByRouteType() {
        Map<RouteType, Integer> themes = new HashMap<>();
        themes.put(RouteType.BOAT, R.style.AlarmSettingsDark_Boat);
        themes.put(RouteType.BUS, R.style.AlarmSettingsLight_Bus);
        themes.put(RouteType.COMMUTER_RAIL, R.style.AlarmSettingsDark_Commuter);

        themes.forEach((key, value) -> {
            Route route = new Route();
            route.setRouteTypeId(key.getId());

            System.out.println("Get theme for route type: " + key);
            assertEquals(value.intValue(), AlertUtils.getTheme(route));
        });
    }

    @Test
    public void getsTextColorForWhiteBackground_textColorIsWhite() {
        Route route = new Route();
        route.setColor("000000");
        route.setTextColor("FFFFFF");

        assertEquals("000000", AlertUtils.getTextColorForWhiteBackground(route));
    }

    @Test
    public void getsTextColorForWhiteBackground_textColorIsNotWhite() {
        Route route = new Route();
        route.setColor("FFFFFF");
        route.setTextColor("000000");

        assertEquals("000000", AlertUtils.getTextColorForWhiteBackground(route));
    }
}
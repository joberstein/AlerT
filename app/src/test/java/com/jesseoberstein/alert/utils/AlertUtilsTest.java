package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.RouteType;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class AlertUtilsTest {
    private Route testRoute;

    @Before
    public void setup() {
        testRoute = new Route();
        testRoute.setId("id");
        testRoute.setLongName("test route");
        testRoute.setShortName("test");
    }

    @Test
    public void getThemeByRouteId() {
        Map<String, Integer> themes = new HashMap<>();
        themes.put("Blue", R.style.AlarmSettingsDark_Blue);
        themes.put("Green-B", R.style.AlarmSettingsDark_Green);
        themes.put("Mattapan", R.style.AlarmSettingsDark_Red);
        themes.put("Orange", R.style.AlarmSettingsDark_Orange);
        themes.put("Red", R.style.AlarmSettingsDark_Red);
        themes.put("SL4", R.style.AlarmSettingsDark_Silver);

        themes.forEach((key, value) -> {
            System.out.println("Get theme for route id: " + key);
            testRoute.setId(key);
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
            testRoute.setRouteType(key);
            assertEquals(value.intValue(), AlertUtils.getTheme(testRoute));
        });
    }
}
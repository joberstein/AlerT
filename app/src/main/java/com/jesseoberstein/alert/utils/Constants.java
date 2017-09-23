package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserRoute;

import java.util.Arrays;
import java.util.List;

import static com.jesseoberstein.mbta.utils.RouteName.*;

public class Constants {

    public static List<ListProp> CUSTOM_LIST_PROPS = Arrays.asList(
            ListProp.ICON, ListProp.PRIMARY_TEXT, ListProp.SECONDARY_TEXT,
            ListProp.TERTIARY_TEXT, ListProp.INFO, ListProp.CHEVRON
    );

    public static List<Integer> CUSTOM_LIST_IDS = Arrays.asList(
            R.id.custom_item_icon, R.id.custom_item_primary_text, R.id.custom_item_secondary_text,
            R.id.custom_item_tertiary_text, R.id.custom_item_info, R.id.custom_item_chevron);

    static final List<UserRoute> USER_ROUTES = Arrays.asList(
            new UserRoute(BLUE.toString(), R.color.blue_line, R.drawable.circle_blue, R.style.BlueLine),
            new UserRoute(GREEN.toString(), R.color.green_line, R.drawable.circle_green, R.style.GreenLine),
            new UserRoute(ORANGE.toString(), R.color.orange_line, R.drawable.circle_orange, R.style.OrangeLine),
            new UserRoute(RED.toString(), R.color.red_line, R.drawable.circle_red, R.style.RedLine),
            new UserRoute(MATTAPAN.toString(), R.color.red_line, R.drawable.circle_red, R.style.RedLine),
            new UserRoute(SILVER.toString(), R.color.silver_line, R.drawable.circle_silver, R.style.SilverLine)
    );

    static final UserRoute DEFAULT_ROUTE =
            new UserRoute("", R.color.default_route, R.drawable.circle_black, R.style.DefaultRoute);

    public static final String THEME = "theme";
    public static final String COLOR = "color";
    public static final String ICON = "icon";
    public static final String ROUTE = "route";

    public static final String ALARM = "alarm";
    public static final String ALARM_ID = "alarmId";
    public static final String NICKNAME = "nickname";
    public static final String ENDPOINTS = "endpoints";
}

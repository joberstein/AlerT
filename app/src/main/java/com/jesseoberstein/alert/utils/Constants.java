package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserRoute;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String MBTA_DATA_DATE = "2018-03-29";

    static final List<UserRoute> USER_ROUTES = Arrays.asList(
//            new UserRoute(BLUE.toString(), R.color.blue_line, R.drawable.circle_blue, R.style.BlueLine),
//            new UserRoute(GREEN.toString(), R.color.green_line, R.drawable.circle_green, R.style.GreenLine),
//            new UserRoute(ORANGE.toString(), R.color.orange_line, R.drawable.circle_orange, R.style.OrangeLine),
//            new UserRoute(RED.toString(), R.color.red_line, R.drawable.circle_red, R.style.RedLine),
//            new UserRoute(MATTAPAN.toString(), R.color.red_line, R.drawable.circle_red, R.style.RedLine),
//            new UserRoute(SILVER.toString(), R.color.silver_line, R.drawable.circle_silver, R.style.SilverLine)
    );

    static final UserRoute DEFAULT_ROUTE =
            new UserRoute("", R.color.default_route, R.drawable.circle_black, R.style.DefaultRoute);

    public static final String THEME = "theme";
    public static final String COLOR = "color";
    public static final String ICON = "icon";
    public static final String ROUTE = "route";
    public static final String CURRENT_TAB = "currentTab";
    public static final int DELAY_DIALOG_DISMISS = 500;

    public static final String ALARM = "alarm";
    public static final String DRAFT_ALARM = "draftAlarm";
    public static final String ALARM_ID = "alarmId";
    public static final String NICKNAME = "nickname";
    public static final String ENDPOINTS = "endpoints";
    public static final String DAYS = "days";
    public static final String STOP_ID = "stopId";
    public static final String DIRECTION_ID = "directionId";

    public static final int ALARM_START_REQUEST_CODE = 4;
    public static final int ALARM_UPDATE_REQUEST_CODE = 5;
    public static final int ALARM_STOP_REQUEST_CODE = 6;
}

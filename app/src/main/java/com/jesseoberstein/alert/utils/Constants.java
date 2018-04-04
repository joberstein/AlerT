package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserRoute;

import java.util.Collections;
import java.util.List;

public class Constants {
    public static final String MBTA_DATA_DATE = "2018-04-04";

    static final List<UserRoute> USER_ROUTES = Collections.EMPTY_LIST;

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

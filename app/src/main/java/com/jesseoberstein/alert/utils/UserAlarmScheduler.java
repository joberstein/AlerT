package com.jesseoberstein.alert.utils;

import android.support.annotation.VisibleForTesting;

import com.jesseoberstein.alert.models.UserAlarm;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.inject.Inject;

public class UserAlarmScheduler {
    private final DateTimeHelper dateTimeHelper;
    private final Calendar calendar;

    @Inject
    public UserAlarmScheduler(DateTimeHelper dateTimeHelper, Calendar calendar) {
        this.dateTimeHelper = dateTimeHelper;
        this.calendar = calendar;
    }

    public void setDefaultAlarmTime(UserAlarm alarm) {
        calendar.setTimeInMillis(dateTimeHelper.getCurrentTime());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        setAlarmTime(alarm, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public void restoreAlarmTime(UserAlarm alarm) {
        if (alarm.getHour() == null || alarm.getMinutes() == null) {
            return;
        }

        long timeInMillis = dateTimeHelper.getTimeInMillis(alarm.getHour(), alarm.getMinutes());
        alarm.setTime(dateTimeHelper.getFormattedTime(timeInMillis));
    }

    public void setAlarmTime(UserAlarm alarm, Integer newHour, Integer newMinutes) {
        if (newHour == null || newMinutes == null) {
            return;
        }

        if (!newHour.equals(alarm.getHour())) {
            alarm.setHour(newHour);
        }

        if (!newMinutes.equals(alarm.getMinutes())) {
            alarm.setMinutes(newMinutes);
        }

        long timeInMillis = dateTimeHelper.getTimeInMillis(newHour, newMinutes);
        alarm.setTime(dateTimeHelper.getFormattedTime(timeInMillis));
        setNextFiringDayString(alarm); // TODO is this right?
    }

    public boolean isTodaySelected(UserAlarm alarm) {
        int[] selectedDays = alarm.getSelectedDays().toIntArray();
        return selectedDays[dateTimeHelper.getCurrentDay() - 1] == 1;
    }

    public void setNextFiringDayString(UserAlarm alarm) {
        if (alarm.getSelectedDays() == null) {
            return;
        }

        String[] weekdayList = DateFormatSymbols.getInstance().getWeekdays();

        long now = dateTimeHelper.getCurrentTime();
        long alarmFiringTime = dateTimeHelper.getTimeInMillis(alarm.getHour(), alarm.getMinutes());
        boolean isPastAlarmFiringTime = alarmFiringTime <= now;

        int nextFiringDay = getNextFiringDay(alarm);
        String nextFiringDayString = weekdayList[nextFiringDay];
        String firingDayTodayString = isPastAlarmFiringTime ? "Next " + nextFiringDayString : "Today";
        boolean isNextFiringDayToday = dateTimeHelper.getCurrentDay() == nextFiringDay;

        String temporalNextFiringDay = isNextFiringDayToday ? firingDayTodayString : nextFiringDayString;
        alarm.setNextFiringDayString(temporalNextFiringDay);
    }

    long getAlarmStartTime(UserAlarm alarm) {
        calendar.set(Calendar.DAY_OF_WEEK, getNextFiringDay(alarm));
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    long getAlarmEndTime(UserAlarm alarm) {
        calendar.setTimeInMillis(getAlarmStartTime(alarm));
        calendar.add(Calendar.MINUTE, Long.valueOf(alarm.getDuration()).intValue());
        return calendar.getTimeInMillis();
    }

    /**
     * Get the day of the week that the given alarm should be fired next by taking into account the
     * alarm's hour/minutes and repeat days.
     * @param alarm The given alarm to calculate the next firing day with.
     * @return A {@link Calendar#DAY_OF_WEEK} value of the next firing day.
     */
    @VisibleForTesting
    int getNextFiringDay(UserAlarm alarm) {
        int today = dateTimeHelper.getCurrentDay();
        long now = dateTimeHelper.getCurrentTime();
        long alarmFiringTime = dateTimeHelper.getTimeInMillis(alarm.getHour(), alarm.getMinutes());
        boolean isPastAlarmFiringTime = alarmFiringTime <= now;
        int[] selectedDays = alarm.getSelectedDays().toIntArray();

        return Stream.of(IntStream.range(today - 1, selectedDays.length), IntStream.range(0, today - 1))
                .flatMap(IntStream::boxed)
                .filter(i -> selectedDays[i] == 1)
                .filter(i -> !(today == (i + 1) && isPastAlarmFiringTime))
                .findFirst()
                .map(zeroIndexedDay -> zeroIndexedDay + 1)
                .orElseGet(() -> {
                    // At this point, either no days have been selected, or only today has been selected.
                    int tomorrow = dateTimeHelper.getTomorrow();
                    return !isTodaySelected(alarm) && isPastAlarmFiringTime ? tomorrow : today;
                });
    }
}

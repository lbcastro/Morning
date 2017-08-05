package pt.castro.mornings2.utils;

import android.content.Context;

import java.util.Calendar;

import pt.castro.mornings2.R;
import pt.castro.mornings2.models.ShowModel;

/**
 * Created by lourenco on 15/06/2017.
 */

public class TimeUtils {

    public static final int SHOW_UPCOMING = 0;
    public static final int SHOW_ONGOING = 1;
    public static final int SHOW_ENDED = 2;
    public static final int SHOW_LATER = 3;

    public static int getShowState(Calendar calendar, ShowModel show) {
        int currentTimeAsMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get
                (Calendar.MINUTE);
        int minutesDifference = currentTimeAsMinutes - show.getMinutesFromMidnight();
        if (minutesDifference >= 0) {
            if (minutesDifference <= 10) {
                return SHOW_ONGOING;
            } else {
                return SHOW_ENDED;
            }
        } else if (minutesDifference >= -10) {
            return SHOW_UPCOMING;
        } else {
            return SHOW_LATER;
        }
    }

    public static String fillTimeEntry(String timeEntry) {
        if (timeEntry.isEmpty()) {
            timeEntry = "00";
        } else if (timeEntry.length() == 1) {
            timeEntry = "0" + timeEntry;
        }
        return timeEntry;
    }

    public static String minutesSinceMidnightToTime(int minutesSinceMidnight) {
        int hours = minutesSinceMidnight / 60;
        int minutes = minutesSinceMidnight % 60 * 60;
        return fillTimeEntry("" + hours) + ":" + fillTimeEntry("" + minutes);
    }

    public static String showTimeToText(int showTime) {
        String time = String.valueOf(showTime);
        char[] chars = time.toCharArray();

        String minutes = "";
        String hours = "";
        for (int x = chars.length - 1; x >= 0; x--) {
            if (chars.length - x < 3) {
                minutes = chars[x] + minutes;
            } else {
                hours = chars[x] + hours;
            }
        }

        hours = fillTimeEntry(hours);
        minutes = fillTimeEntry(minutes);

        return hours + ":" + minutes;
    }

    public static String calendarIntToLabel(Context context, int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return context.getString(R.string.sunday);
            case Calendar.MONDAY:
                return context.getString(R.string.monday);
            case Calendar.TUESDAY:
                return context.getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return context.getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return context.getString(R.string.thursday);
            case Calendar.FRIDAY:
                return context.getString(R.string.friday);
            case Calendar.SATURDAY:
                return context.getString(R.string.saturday);
            default:
                return "";
        }
    }
}
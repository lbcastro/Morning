package pt.castro.mornings2;

import java.util.Calendar;

/**
 * Created by lourenco on 31/05/2017.
 */

public class FirebaseKeys {

    public static final String SHOWS = "shows";
    public static final String TIMETABLE = "timetable";
    public static final String RADIOS = "radios";

    static String calendarIntToKey(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "sun";
            case Calendar.MONDAY:
                return "mon";
            case Calendar.TUESDAY:
                return "tue";
            case Calendar.WEDNESDAY:
                return "wed";
            case Calendar.THURSDAY:
                return "thu";
            case Calendar.FRIDAY:
                return "fri";
            case Calendar.SATURDAY:
                return "sat";
            default:
                return "";
        }
    }
}

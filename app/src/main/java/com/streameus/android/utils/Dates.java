package com.streameus.android.utils;

import android.text.format.Time;

/**
 * Created by Pol on 10/07/14.
 */
public class Dates {

    /**
     * Format like: "08/03/1992"
     * @param date
     * @return
     */
    public static String parse3339ToReadable(String date) {
        Time t = new Time(Time.getCurrentTimezone());
        t.parse3339(date);
        return t.format("%d/%m/%Y %k:%m");
    }

    /**
     * Format like: "Dimanche 8 mars 1992"
     * @param date
     * @return
     */
    public static String parse3339ToReadableText(String date) {
        Time t = new Time(Time.getCurrentTimezone());
        t.parse3339(date);
        return capitalizeFirstLetter(t.format("%e %B %Y"));
    }

    /**
     * Capitalize first letter
     * @param original
     * @return
     */
    public static String capitalizeFirstLetter(String original){
        if (original == null || original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static int compateTime(String t1, String t2) {
        Time tt1 = new Time(Time.getCurrentTimezone());
        tt1.parse3339(t1);
        Time tt2 = new Time(Time.getCurrentTimezone());
        tt2.parse3339(t2);
        return Time.compare(tt1, tt2);
    }

    public static int compareTime(String t1) {
        Time tt1 = new Time(Time.getCurrentTimezone());
        tt1.parse3339(t1);
        Time t2 = new Time(Time.getCurrentTimezone());
        t2.setToNow();
        return Time.compare(tt1, t2);
    }
}

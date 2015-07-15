package com.streameus.android.utils;

import java.util.Date;

/**
 * Created by Pol on 12/12/14.
 */
public class DateUtils {

    public static String diffTime(Date toSustract, Date origin) {
        long diff = origin.getTime() - toSustract.getTime();
        diff /= 1000;
        if (Math.abs(diff) <= 60) {
            return "just now";
        }

        diff /= 60;
         if (Math.abs(diff) < 60) {
             return diff  + " min ago";
         }

        diff /= 60;
        if (Math.abs(diff) < 24) {
            return diff  + " h ago";
        }

        diff /= 24;
        if (Math.abs(diff) < 7) {
            return diff  + " d";
        }
        diff /= 7;
        return diff + " w";
    }
}

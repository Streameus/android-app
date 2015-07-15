package com.streameus.android.utils;

import android.content.Context;

/**
 * Created by Pol on 26/01/15.
 */
public class StreameusPreferences {

    private static final String STREAMEUS_PREFS = "STREAMEUS_PREFS";
    private static final String HAVE_ALREADY_SEEN_DEMO = "HAVE_ALREADY_SEEN_DEMO";
    private static final String SHARED_TOKEN = "SHARED_TOKEN";

    public static boolean hasAlreadySeenDemo(Context context) {
        return context.getSharedPreferences(STREAMEUS_PREFS,
                Context.MODE_MULTI_PROCESS).getBoolean(HAVE_ALREADY_SEEN_DEMO, false);
    }

    public static void setHaveAlreadySeenDemo(Context context, boolean value) {
       context.getSharedPreferences(STREAMEUS_PREFS,
                Context.MODE_MULTI_PROCESS).edit().putBoolean(HAVE_ALREADY_SEEN_DEMO, value).apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(STREAMEUS_PREFS, Context.MODE_MULTI_PROCESS).getString(SHARED_TOKEN, "");
    }

    public static void setToken(Context context, String token) {
        context.getSharedPreferences(STREAMEUS_PREFS,
                Context.MODE_MULTI_PROCESS).edit().putString(SHARED_TOKEN, token).apply();
    }
}
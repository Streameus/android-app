package com.streameus.android.model;

import android.text.format.Time;

/**
 * Created by Pol on 09/07/14.
 */
public class ConferenceSmall implements IConference{

    private int Id;
    private String Name;
    private String Date;
    private long timestamp;

    public long getTimestamp() {
        if (timestamp == 0) {
            Time t = new Time();
            t.parse3339(Date);
            timestamp = t.toMillis(false);
        }
        return timestamp;
    }


    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getDate() {
        return Date;
    }
}

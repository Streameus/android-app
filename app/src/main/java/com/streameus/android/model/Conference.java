package com.streameus.android.model;

import android.text.format.Time;

/**
 * Created by Pol on 09/07/14.
 */
public class Conference implements IConference {
    private int Id;
    private int Owner;
    private String Name;
    private String Description;
    private String Status; //['EnCours' or 'AVenir' or 'Finie']: Status of the conference,
    private String Time;
    private int ScheduledDuration;
    private int FinalDuration;
    private ConferenceCategorie Category;

    private boolean Registered;

    private long timestamp;

    public long getTimestamp() {
        if (timestamp == 0) {
            Time t = new Time();
            t.parse3339(this.Time);
            timestamp = t.toMillis(false);
        }
        return timestamp;
    }

    public int getId() {
        return Id;
    }

    public int getOwner() {
        return Owner;
    }

    public String getName() {
        return Name;
    }

    @Override
    public String getDate() {
        return this.Time;
    }

    public String getDescription() {
        return Description;
    }

    public String getStatus() {
        return Status;
    }

    public String getTime() {
        return Time;
    }

    public int getScheduledDuration() {
        return ScheduledDuration;
    }

    public int getFinalDuration() {
        return FinalDuration;
    }

    public ConferenceCategorie getCategory() {
        return Category;
    }

    public boolean isRegistered() {
        return Registered;
    }

    public void setRegistered(boolean registered) {
        Registered = registered;
    }
}

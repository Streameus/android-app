package com.streameus.android.model;

/**
 * Created by Pol on 09/07/14.
 */
public class EventInfo {
    public static enum InfoType {USER, COMMENT, CONFERENCE, INT, DATETIME, STRING, UNDEFINED};
    public static final int TYPE_USER = 1;
    public static final int TYPE_CONFERENCE = 3;

    private int Id;
    private int TargetId;
    int TargetType;
    private int Pos;
    private String Content;




    public int getId() {
        return Id;
    }

    public int getTargetId() {
        return TargetId;
    }

    public int getTargetType() {
        return TargetType;
    }

    public int getPos() {
        return Pos;
    }

    public String getContent() {
        return Content;
    }
}

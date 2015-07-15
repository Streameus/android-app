package com.streameus.android.bus;

/**
 * Created by Pol on 23/11/14.
 */
public class Event {

    protected String errorMSG;

    protected Event() {

    }

    protected Event(String errorMSG) {
        this.errorMSG = (errorMSG == null ? "Error happend" : errorMSG);
    }

    public String getErrorMSG() {
        return errorMSG;
    }
}

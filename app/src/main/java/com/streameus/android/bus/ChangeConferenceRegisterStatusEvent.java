package com.streameus.android.bus;

/**
 * Created by Pol on 03/08/14.
 */
public class ChangeConferenceRegisterStatusEvent {
    private int conferenceID;
    private boolean register;

    public ChangeConferenceRegisterStatusEvent(int conferenceID, boolean register) {
        this.conferenceID = conferenceID;
        this.register = register;
    }

    public int getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(int conferenceID) {
        this.conferenceID = conferenceID;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

}

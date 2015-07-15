package com.streameus.android.bus;

import com.streameus.android.model.Conference;
import com.streameus.android.model.User;

public class ReceiveLectureEvent extends Event {
    private Conference conference;
    private User conferencier;

    public ReceiveLectureEvent(Conference conference, User conferencier) {
        this.conference = conference;
        this.conferencier = conferencier;
    }

    public ReceiveLectureEvent(String errorMSG) {
        super(errorMSG);
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public User getConferencier() {
        return conferencier;
    }

    public void setConferencier(User conferencier) {
        this.conferencier = conferencier;
    }

}

package com.streameus.android.bus;

import com.streameus.android.model.ConferenceSet;

import java.util.List;

/**
 * Created by Pol on 08/05/14.
 */
public class ReceiveAgendaEvent extends Event {
    private List<ConferenceSet> conferenceSmallList;


    public ReceiveAgendaEvent(List<ConferenceSet> e) {
        conferenceSmallList = e;
    }

    public ReceiveAgendaEvent(String errorMSG) {
        super(errorMSG);
    }

    public List<ConferenceSet> getConferenceSmallList() {
        return conferenceSmallList;
    }
}

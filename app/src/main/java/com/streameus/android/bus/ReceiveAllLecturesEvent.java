package com.streameus.android.bus;
import com.streameus.android.gui.ConferencesListFragment;
import com.streameus.android.model.Conference;

import java.util.List;

public class ReceiveAllLecturesEvent extends Event {
    private ConferencesListFragment.ConferenceListType type;

    private List<Conference> iConferenceList;

    public ReceiveAllLecturesEvent(ConferencesListFragment.ConferenceListType type, List<Conference> iConferenceList) {
        this.type = type;
        this.iConferenceList = iConferenceList;
    }


    public ReceiveAllLecturesEvent(ConferencesListFragment.ConferenceListType type, String ErrorMsg) {
        super(ErrorMsg);
        this.type = type;
    }

    public List<Conference> getConferenceList() {
        return iConferenceList;
    }

    public ConferencesListFragment.ConferenceListType getType() {
        return type;
    }


    public String getErrorMSG() {
        return errorMSG;
    }
}

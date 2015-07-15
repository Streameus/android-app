package com.streameus.android.bus;


import com.streameus.android.gui.ConferencesListFragment;

/**
 * Created by Pol on 08/05/14.
 */
public class GetLecturesEvent {
    ConferencesListFragment.ConferenceListType type;

    int paramValue;

    public GetLecturesEvent(ConferencesListFragment.ConferenceListType type, int userID) {
        this.type = type;
        this.paramValue = userID;
    }

    public ConferencesListFragment.ConferenceListType getType() {
        return type;
    }

    public void setType(ConferencesListFragment.ConferenceListType type) {
        this.type = type;
    }

    public int getParamValue() {
        return paramValue;
    }

    public void setParamValue(int paramValue) {
        this.paramValue = paramValue;
    }
}

package com.streameus.android.bus;

import com.streameus.android.model.ConferenceCategorie;

import java.util.List;

/**
 * Created by Pol on 30/10/14.
 */
public class ReceiveConferenceCategories {
    List<ConferenceCategorie> conferenceCategorieList;


    public ReceiveConferenceCategories(List<ConferenceCategorie> conferenceCategorieList) {
        this.conferenceCategorieList = conferenceCategorieList;
    }


    public List<ConferenceCategorie> getConferenceCategorieList() {
        return conferenceCategorieList;
    }
}

package com.streameus.android.model;

import java.util.List;

/**
 * Created by Pol on 28/10/14.
 */
public class SearchWrapper {

    private List<User> Users;
    private List<Conference> Conferences;

    public SearchWrapper(List<User> users, List<Conference> conferences) {
        Users = users;
        Conferences = conferences;
    }

    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> users) {
        Users = users;
    }

    public List<Conference> getConferences() {
        return Conferences;
    }

    public void setConferences(List<Conference> conferences) {
        Conferences = conferences;
    }
}

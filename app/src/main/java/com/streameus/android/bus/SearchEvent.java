package com.streameus.android.bus;

/**
 * Created by Pol on 28/10/14.
 */
public class SearchEvent {
    private String searchQuery;

    public SearchEvent(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

}

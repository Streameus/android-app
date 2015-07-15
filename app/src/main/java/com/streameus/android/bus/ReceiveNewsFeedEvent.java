package com.streameus.android.bus;

import com.streameus.android.model.EventItem;

import java.util.List;

/**
 * Created by Pol on 08/05/14.
 */
public class ReceiveNewsFeedEvent extends Event {
    private List<EventItem> eventItemList;


    public ReceiveNewsFeedEvent(List<EventItem> e) {
        eventItemList = e;
    }
    public ReceiveNewsFeedEvent(String errorMSG) {
        super(errorMSG);
    }
    public List<EventItem> getEventItemList() {
        return eventItemList;
    }
}

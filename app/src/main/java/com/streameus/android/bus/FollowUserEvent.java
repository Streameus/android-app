package com.streameus.android.bus;

/**
 * Created by Pol on 08/05/14.
 */
public class FollowUserEvent {
    private int mID;

    public FollowUserEvent(int id) {
        mID = id;
    }

    public int getID() {
        return mID;
    }
}

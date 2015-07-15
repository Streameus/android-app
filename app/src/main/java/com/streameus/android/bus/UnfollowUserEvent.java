package com.streameus.android.bus;

/**
 * Created by Pol on 08/05/14.
 */
public class UnfollowUserEvent {
    private int mID;

    public UnfollowUserEvent(int id) {
        mID = id;
    }

    public int getID() {
        return mID;
    }
}

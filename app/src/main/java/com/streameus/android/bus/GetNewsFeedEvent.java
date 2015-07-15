package com.streameus.android.bus;

/**
 * Created by Pol on 08/05/14.
 */
public class GetNewsFeedEvent {
    private int mID;
    private long top = 0;
    private long skip = 0;

    public GetNewsFeedEvent(int id) {
        mID = id;
    }

    public GetNewsFeedEvent(int mID, long top, long skip) {
        this.mID = mID;
        this.top = top;
        this.skip = skip;
    }

    public int getID() {
        return mID;
    }

    public long getTop() {
        return top;
    }

    public long getSkip() {
        return skip;
    }
}

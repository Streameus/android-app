package com.streameus.android.bus;

/**
 * Created by Pol on 08/05/14.
 */
public class GetProfilDataEvent {
    private int mID;
    private boolean mAskingFriendshipRelation;


    public GetProfilDataEvent(int id, Boolean askFriendship) {
        mID = id;
        mAskingFriendshipRelation = askFriendship;
    }

    public int getID() {
        return mID;
    }

    public boolean isAskingFriendshipRelation() {
        return mAskingFriendshipRelation;
    }

}

package com.streameus.android.bus;

/**
 * Created by Pol on 08/05/14.
 */
public class GetConferenceEvent {
    private int mID;
   // private boolean mAskingFriendshipRelation;


    public GetConferenceEvent(int id) {
        mID = id;
       // mAskingFriendshipRelation = askFriendship;
    }

    public int getID() {
        return mID;
    }

   // public boolean isAskingFriendshipRelation() {
//        return mAskingFriendshipRelation;
//    }

}

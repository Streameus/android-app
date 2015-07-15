package com.streameus.android.bus;

import com.streameus.android.model.User;

/**
 * Created by Pol on 08/05/14.
 */
public class ReceiveProfilDataEvent extends Event {

    private User mUser;
    private boolean mIsFollowing = false;
    private boolean mRelationAsked = false;

    public ReceiveProfilDataEvent(User user, boolean relationAsked , boolean isFollowing) {
        mUser = user;
        mIsFollowing = isFollowing;
        mRelationAsked = relationAsked;
    }


    public ReceiveProfilDataEvent(String errorMSG) {
        super(errorMSG);
    }

    public User getUser() {
        return mUser;
    }

    public boolean isFollowing() {
        return mIsFollowing;
    }

    public boolean isRelationAsked() {
        return mRelationAsked;
    }
}


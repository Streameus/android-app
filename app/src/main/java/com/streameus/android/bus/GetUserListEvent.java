package com.streameus.android.bus;

import com.streameus.android.gui.UsersListFragment;

/**
 * Created by Pol on 08/05/14.
 */
public class GetUserListEvent {
    UsersListFragment.UserListType type;

    private int mID;

    public GetUserListEvent(UsersListFragment.UserListType type, int mID) {
        this.type = type;
        this.mID = mID;
    }

    public UsersListFragment.UserListType getType() {
        return type;
    }

    public void setType(UsersListFragment.UserListType type) {
        this.type = type;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }
}

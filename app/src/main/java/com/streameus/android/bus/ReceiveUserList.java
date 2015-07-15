package com.streameus.android.bus;

import com.streameus.android.gui.UsersListFragment;
import com.streameus.android.model.User;

import java.util.List;

/**
 * Created by Pol on 08/05/14.
 */
public class ReceiveUserList extends Event {
    UsersListFragment.UserListType type;
    List<User> userList;

    public ReceiveUserList(UsersListFragment.UserListType type, List<User> userList) {
        this.type = type;
        this.userList = userList;
    }

    public ReceiveUserList(UsersListFragment.UserListType type, String error) {
        super(error);
        this.type = type;
    }

    public UsersListFragment.UserListType getType() {
        return type;
    }

    public void setType(UsersListFragment.UserListType type) {
        this.type = type;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}


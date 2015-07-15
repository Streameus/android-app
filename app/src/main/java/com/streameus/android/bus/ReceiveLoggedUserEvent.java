package com.streameus.android.bus;

import com.streameus.android.model.User;

/**
 * Created by Pol on 08/05/14.
 */
public class ReceiveLoggedUserEvent extends Event {
    private User u;

    public ReceiveLoggedUserEvent(User user) {
        u = user;
    }

    public ReceiveLoggedUserEvent(String errorMSG) {
        super(errorMSG);
    }

    public User getUser() {
        return u;
    }
}

package com.streameus.android.bus;

/**
 * Created by Pol on 08/05/14.
 */
public class GetLoggedUserEvent {
    String token = "";

    public GetLoggedUserEvent( String t) {
        token = t;
    }


    public String getToken() {
        return token;
    }
}

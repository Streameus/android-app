package com.streameus.android.bus;

/**
 * Created by Pol on 05/09/14.
 */
public class ReceiveTokenEvent {
    String token;

    public ReceiveTokenEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

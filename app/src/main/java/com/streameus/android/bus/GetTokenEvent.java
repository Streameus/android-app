package com.streameus.android.bus;

/**
 * Created by Pol on 05/09/14.
 */
public class GetTokenEvent {

    private String login;
    private String password;

    public GetTokenEvent(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

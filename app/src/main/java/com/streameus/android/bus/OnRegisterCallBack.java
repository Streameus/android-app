package com.streameus.android.bus;

/**
 * Created by Pol on 07/11/14.
 */
public class OnRegisterCallBack extends Event {

    private String userName;
    private String password;
    private String errorMSG;

    public OnRegisterCallBack(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public OnRegisterCallBack(String errorMSG) {
        super(errorMSG);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}

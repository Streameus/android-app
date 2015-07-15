package com.streameus.android.bus;

/**
 * Created by Pol on 07/11/14.
 */
public class RegisterEvent {
    private String pseudo;
    private String email;
    private String Password;
    private String ComfirmPassword;

    public RegisterEvent(String pseudo, String email, String password, String comfirmPassword) {
        this.pseudo = pseudo;
        this.email = email;
        Password = password;
        ComfirmPassword = comfirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getComfirmPassword() {
        return ComfirmPassword;
    }

    public void setComfirmPassword(String comfirmPassword) {
        ComfirmPassword = comfirmPassword;
    }

    public String getPseudo() {
        return pseudo;
    }
}

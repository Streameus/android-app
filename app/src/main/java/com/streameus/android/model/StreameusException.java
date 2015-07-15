package com.streameus.android.model;

/**
 * Created by Pol on 20/11/14.
 */
public class StreameusException extends Exception {
    String errorMSG;

    public StreameusException(String errorMSG) {
        this.errorMSG = errorMSG;
    }

    public String getErrorMSG() {
        return errorMSG;
    }

    public void setErrorMSG(String errorMSG) {
        this.errorMSG = errorMSG;
    }
}

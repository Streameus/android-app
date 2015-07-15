package com.streameus.android.model;

import java.util.List;

/**
 * Created by Pol on 10/07/14.
 */
public class ConferenceSet {
    private String Key;
    private List<ConferenceSmall> Value;

    public String getKey() {
        return Key;
    }

    public List<ConferenceSmall> getValue() {
        return Value;
    }
}

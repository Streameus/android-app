package com.streameus.android.model;

import java.util.Comparator;

/**
 * Created by Pol on 10/07/14.
 */
public class IConferenceComparator implements Comparator<IConference> {
    @Override
    public int compare(IConference o1, IConference o2) {
        long out = o1.getTimestamp() - o2.getTimestamp();
        if (out < 0)
            return -1;
        if (out == 0)
            return 0;
        return 1;
    }
}
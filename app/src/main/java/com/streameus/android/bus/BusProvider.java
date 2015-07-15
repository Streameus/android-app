package com.streameus.android.bus;

import com.squareup.otto.Bus;

/**
 * Created by Pol on 06/05/14.
 */
public  class BusProvider {
    private static Bus bus = new Bus();

    public static Bus get() {
        return bus;
    }
}

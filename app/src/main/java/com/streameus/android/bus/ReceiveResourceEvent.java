package com.streameus.android.bus;

import com.streameus.android.model.StreameusResources;

/**
 * Created by Pol on 08/05/14.
 */
public class ReceiveResourceEvent extends Event{
    private StreameusResources mStreameusResources;


    public ReceiveResourceEvent(StreameusResources streameusResources) {
        mStreameusResources = streameusResources;
    }

    public ReceiveResourceEvent(String errorMSG) {
        super(errorMSG);
    }

    public StreameusResources get() {
        return mStreameusResources;
    }
}

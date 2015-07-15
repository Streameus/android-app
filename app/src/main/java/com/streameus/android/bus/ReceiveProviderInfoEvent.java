package com.streameus.android.bus;


import com.streameus.android.model.OAuthProviderInfo;

import java.util.List;


public class ReceiveProviderInfoEvent extends Event{
    private List<OAuthProviderInfo> mProviderInfo;

    public ReceiveProviderInfoEvent(List<OAuthProviderInfo> providerInfo) {
        mProviderInfo = providerInfo;
    }

    public ReceiveProviderInfoEvent(String errorMSG) {
        super(errorMSG);
    }

    public List<OAuthProviderInfo> get() {
        return mProviderInfo;
    }
}

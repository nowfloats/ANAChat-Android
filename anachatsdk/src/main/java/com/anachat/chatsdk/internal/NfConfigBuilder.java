package com.anachat.chatsdk.internal;


import android.content.Context;

import com.anachat.chatsdk.NfChatSDKConfig;
import com.anachat.chatsdk.ConfigBuilder;

public class NfConfigBuilder implements ConfigBuilder {
    private Context mContext;

    public ConfigBuilder context(Context newContext) {
        if (newContext == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }
        this.mContext = newContext;
        return this;

    }


    public NfChatSDKConfig build() {
        return new DefaultNfChatSDKConfig(this.mContext);
    }
}

package com.nowfloats.chatsdk.internal;


import android.content.Context;

import com.nowfloats.chatsdk.ConfigBuilder;
import com.nowfloats.chatsdk.NfChatSDKConfig;

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

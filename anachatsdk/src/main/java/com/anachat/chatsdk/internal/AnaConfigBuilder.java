package com.anachat.chatsdk.internal;


import android.content.Context;

import com.anachat.chatsdk.AnaChatSDKConfig;
import com.anachat.chatsdk.ConfigBuilder;

public class AnaConfigBuilder implements ConfigBuilder {
    private Context mContext;

    public ConfigBuilder context(Context newContext) {
        if (newContext == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }
        this.mContext = newContext;
        return this;

    }


    public AnaChatSDKConfig build() {
        return new DefaultAnaChatSDKConfig(this.mContext);
    }
}

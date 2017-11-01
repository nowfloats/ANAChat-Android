package com.anachat.chatsdk.internal;

import android.content.Context;

import com.anachat.chatsdk.AnaChatSDKConfig;

public class DefaultAnaChatSDKConfig implements AnaChatSDKConfig {
    private Context mContext;

    public DefaultAnaChatSDKConfig(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }
}

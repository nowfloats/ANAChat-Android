package com.anachat.chatsdk.internal;

import android.content.Context;

import com.anachat.chatsdk.NfChatSDKConfig;

public class DefaultNfChatSDKConfig implements NfChatSDKConfig {
    private Context mContext;

    public DefaultNfChatSDKConfig(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }
}

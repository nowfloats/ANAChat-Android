package com.nowfloats.chatsdk.internal;

import android.content.Context;

import com.nowfloats.chatsdk.NfChatSDKConfig;

public class DefaultNfChatSDKConfig implements NfChatSDKConfig {
    private Context mContext;

    public DefaultNfChatSDKConfig(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }
}

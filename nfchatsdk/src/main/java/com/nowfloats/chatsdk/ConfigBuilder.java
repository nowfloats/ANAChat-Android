package com.nowfloats.chatsdk;

import android.content.Context;

public interface ConfigBuilder {
    ConfigBuilder context(Context paramContext);

    NfChatSDKConfig build();
}



package com.anachat.chatsdk;

import android.content.Context;

public interface ConfigBuilder {
    ConfigBuilder context(Context paramContext);

    AnaChatSDKConfig build();
}



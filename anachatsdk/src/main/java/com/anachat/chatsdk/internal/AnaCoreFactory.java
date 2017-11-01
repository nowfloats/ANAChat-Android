package com.anachat.chatsdk.internal;

import com.anachat.chatsdk.AnaChatSDKConfig;
import com.anachat.chatsdk.MessageListener;

public class AnaCoreFactory {

    public static void create(AnaChatSDKConfig config, MessageListener listener) {
        new MessengerCoreMethods(config, listener);
    }
}

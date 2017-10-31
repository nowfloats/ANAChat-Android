package com.anachat.chatsdk.internal;

import com.anachat.chatsdk.NfChatSDKConfig;
import com.anachat.chatsdk.MessageListener;

public class AnaCoreFactory {

    public static void create(NfChatSDKConfig config, MessageListener listener) {
        new MessengerCoreMethods(config, listener);
    }
}

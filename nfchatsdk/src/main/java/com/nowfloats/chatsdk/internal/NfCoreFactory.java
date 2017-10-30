package com.nowfloats.chatsdk.internal;

import com.nowfloats.chatsdk.MessageListener;
import com.nowfloats.chatsdk.NfChatSDKConfig;

public class NfCoreFactory {

    public static void create(NfChatSDKConfig config, MessageListener listener) {
        new MessengerCoreMethods(config, listener);
    }
}

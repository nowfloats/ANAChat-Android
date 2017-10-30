package com.nowfloats.chatsdk.sample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.nowfloats.chatsdk.NfCore;
import com.nowfloats.chatsdk.internal.utils.NFChatSDK;

/**
 * Created by lookup on 30/08/17.
 */

public class NfChatApplication extends Application {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

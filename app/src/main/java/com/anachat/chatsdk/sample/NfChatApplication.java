package com.anachat.chatsdk.sample;

import android.app.Application;
import android.content.Context;

/**
 * Created by lookup on 30/08/17.
 */

public class NfChatApplication extends Application {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }
}

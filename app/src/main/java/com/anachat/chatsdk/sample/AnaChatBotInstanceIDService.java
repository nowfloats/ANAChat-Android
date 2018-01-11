package com.anachat.chatsdk.sample;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.anachat.chatsdk.AnaCore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by lookup on 30/08/17.
 */

public class AnaChatBotInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        AnaCore.updateToken(this, refreshedToken,
                LaunchActivity.BASE_URL, LaunchActivity.BUSINESSID, "");
    }
}


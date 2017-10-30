package com.nowfloats.chatsdk.sample;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nowfloats.chatsdk.NfCore;

/**
 * Created by lookup on 30/08/17.
 */

public class NfChatBotInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        NfCore.updateToken(this, refreshedToken);
    }
}

package com.anachat.chatsdk.sample;

import com.anachat.chatsdk.AnaCore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by lookup on 30/08/17.
 */

public class AnaChatBotInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token, Save this token in your preferences or database
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //When user is not registered
        AnaCore.saveFcmToken(this, refreshedToken);
        //When user registered
      //  AnaCore.saveFcmToken(this, refreshedToken,"user_id");
    }
}


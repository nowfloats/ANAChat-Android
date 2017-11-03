package com.anachat.chatsdk.sample;

import com.anachat.chatsdk.AnaCore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


/**
 * Created by lookup on 30/08/17.
 */

public class AnaChatBotMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> mapResult = remoteMessage.getData();
        if (mapResult.containsKey("payload")) {
            AnaCore.handlePush(this, mapResult.get("payload"));
        }
    }

}

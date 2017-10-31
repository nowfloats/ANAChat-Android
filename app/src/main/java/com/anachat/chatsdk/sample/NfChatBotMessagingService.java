package com.anachat.chatsdk.sample;

import android.content.Context;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.anachat.chatsdk.AnaCore;
import com.anachat.chatsdk.internal.database.MessageRepository;
import com.anachat.chatsdk.internal.model.MessageResponse;

import java.util.Map;


/**
 * Created by lookup on 30/08/17.
 */

public class NfChatBotMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> mapResult = remoteMessage.getData();
        if (mapResult.containsKey("payload")) {
            AnaCore.handlePush(this, mapResult.get("payload"));
        }

    }

    public static void addMessage(Context context) {
        try {
            MessageResponse messageResponse = new Gson().fromJson("{\n" +
                    "\"meta\": {\n" +
                    "\"id\": \"679aee65-aa66-4fab-88b8-786b762624d1\",\n" +
                    "\"sender\": {\"id\":\"sender\",\"medium\":1},\n" +
                    "\"recipient\": {\"id\":\"sender\",\"medium\":1},\n" +
                    "\"senderType\": 1,\n" +
                    "\"timestamp\": 1508151859853,\n" +
                    "\"sessionId\": \"9abc9f37-8a3a-40b5-8a70-0cb57d251018\",\n" +
                    "\"responseTo\": \"34d8d547-86bb-4d0f-a92c-5e56ec2f9e23\"\n" +
                    "},\n" +
                    "\"data\": {\n" +
                    "\"type\": 0,\n" +
                    "\"content\": {\n" +
                    "\"text\": \"This is a sample text\",\n" +
                    "\"mandatory\": 1\n" +
                    "}\n" +
                    "}\n" +
                    "}", MessageResponse.class);
            int messageType = messageResponse.getData().getType();
            messageResponse.getMessage().setMessageType(messageType);
            messageResponse.getMessage().setSyncWithServer(true);
            MessageRepository.getInstance(context).handleMessageResponse(messageResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

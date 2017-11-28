package com.anachat.chatsdk.sample;

import android.content.Context;

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

    public static void addMessage(Context context) {
//        String m = "{\n" +
//                "  \"meta\": {\n" +
//                "    \"id\": \"12fb3322-d4a7-4313-8d25-22a9755119cb8967\",\n" +
//                "    \"sender\": {\n" +
//                "      \"id\": \"358987017890524\",\n" +
//                "      \"medium\": 1\n" +
//                "    },\n" +
//                "    \"recipient\": {\n" +
//                "      \"id\": \"fbbc77cd-e34a-446a-8943-de2db0ff42ce\",\n" +
//                "      \"medium\": 1\n" +
//                "    },\n" +
//                "    \"senderType\": 0,\n" +
//                "    \"timestamp\": 1511291523777,\n" +
//                "    \"sessionId\": \"3d33d73b-fe8e-44b4-be37-e8c1b039f658\",\n" +
//                "    \"responseTo\": \"607485f8-cd12-4216-8dbd-1be9726808d9\"\n" +
//                "  },\n" +
//                "  \"data\": {\n" +
//                "    \"type\": 2,\n" +
//                "    \"content\": {\n" +
//                "      \"mediaType\": 0,\n" +
//                "      \"inputType\": 8\n" +
//                "    }\n" +
//                "  }\n" +
//                "}";
//        String m ="{\n" +
//                "\"meta\": {\n" +
//                "\"id\": \"069abb0b-e864-4c00-ba18-c0e2c61c4cc0\",\n" +
//                "\"sender\": {\n" +
//                "\"id\":\"358987017890524\",\n" +
//                "\"medium\":1},\n" +
//                "\"recipient\": {\n" +
//                "\"id\":\"fbbc77cd-e34a-446a-8943-de2db0ff42ce\",\n" +
//                "\"medium\":1},\n" +
//                "\"senderType\": 1,\n" +
//                "\"timestamp\": 1511438049999,\n" +
//                "\"sessionId\": \"57f38fbe-069a-43c2-926e-75b17d80b5d6\",\n" +
//                "\"responseTo\": \"9c254ac0-5948-402b-9fc3-6e8abfab301e\"\n" +
//                "},\n" +
//                "\"data\": {\n" +
//                "\"type\": 2,\n" +
//                "\"content\": {\n" +
//                "\"inputType\": 7,\n" +
//                "\"mandatory\": 1,\n" +
//                "\"defaultLocation\": {\n" +
//                "\"lat\": 17.428208,\n" +
//                "\"lng\": 78.3898997\n" +
//                "}}\n" +
//                "}\n" +
//                "}";
//        AnaCore.handlePush(context, m);

    }
}

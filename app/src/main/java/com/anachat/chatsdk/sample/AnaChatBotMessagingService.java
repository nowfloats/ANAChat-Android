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

//    public static void addMessage(Context context) {
//        String m ="{\n" +
//                "  \"meta\": {\n" +
//                "    \"id\": \"12fb3322-d4a7-4313-8d25-22a9755119cb8967\",\n" +
//                "    \"sender\": {\n" +
//                "      \"id\": \"business\",\n" +
//                "      \"medium\": 1\n" +
//                "    },\n" +
//                "    \"recipient\": {\n" +
//                "      \"id\": \"f0308b0c-1b37-4297-8f7d-6634d8371145\",\n" +
//                "      \"medium\": 1\n" +
//                "    },\n" +
//                "    \"senderType\": 0,\n" +
//                "    \"timestamp\": 1513589225635,\n" +
//                "    \"sessionId\": \"3d33d73b-fe8e-44b4-be37-e8c1b039f658\",\n" +
//                "    \"responseTo\": \"607485f8-cd12-4216-8dbd-1be9726808d9\"\n" +
//                "  },\n" +
//                "  \"data\": {\n" +
//                "    \"type\": 2,\n" +
//                "    \"content\": {\n" +
//                "      \"mediaType\": 0,\n" +
//                "      \"inputType\": 8\n" +
//                "      \n" +
//                "    }\n" +
//                "  }\n" +
//                "}";
//        AnaCore.handlePush(context, m);
//
//    }
}

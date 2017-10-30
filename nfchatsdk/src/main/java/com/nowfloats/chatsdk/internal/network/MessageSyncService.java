//package com.nowfloats.chatsdk.internal.network;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.nowfloats.chatsdk.internal.utils.constants.Constants;
//import com.nowfloats.chatsdk.internal.database.MessageRepository;
//import com.nowfloats.chatsdk.internal.model.MessageResponse;
//
//import java.io.IOException;
//
//public class MessageSyncService extends IntentService {
//
//    private static final String TAG = "MessageSyncService";
//
//    public MessageSyncService() {
//        super(MessageSyncService.class.getName());
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Log.d(TAG, "Service Started!");
//        Bundle bundle = intent.getExtras();
//        MessageResponse messageResponse = (MessageResponse) bundle.getSerializable(Constants.MESSAGE_RESPONSE);
//        if (messageResponse != null) {
//            /* Update UI: Download Service is Running */
//            syncMessage(messageResponse);
//        }
//        Log.d(TAG, "Service Stopping!");
//        this.stopSelf();
//    }
//
//    private void syncMessage(final MessageResponse messageResponse) {
//        try {
//            MessageResponse response
//                    = NFChatService.Creator.newNFAPIService()
//                    .sendMessage(messageResponse)
//                    .execute().body();
//            if (response != null &&
//                    response.getMessage() != null) {
//                response.getMessage().setMessageType(
//                        response.getData().getType());
//                response.getMessage().setSyncWithServer(true);
//                MessageRepository.getInstance(getApplicationContext()).
//                        handleMessageResponse(response);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        NFChatService.Creator.newNFAPIService().sendMessage(messageResponse).
////                enqueue(new Callback<MessageResponse>() {
////                    @Override
////                    public void onResponse(Call<MessageResponse> call,
////                                           Response<MessageResponse> response) {
////                        if (response.isSuccessful() && response.body() != null &&
////                                response.body().getMessage() != null) {
////                            response.body().getMessage().setMessageType(
////                                    response.body().getData().getType());
////                            response.body().getMessage().setSyncWithServer(true);
////                            MessageRepository.getInstance(getApplicationContext()).
////                                    handleMessageResponse(response.body());
////                        }
////                    }
////
////                    @Override
////                    public void onFailure(Call<MessageResponse> call, Throwable t) {
////                        Log.d(TAG, "Error in API!" + t.getMessage());
////                    }
////                });
//    }
//}
package com.nowfloats.chatsdk;

import android.content.Context;

import com.google.gson.Gson;
import com.nowfloats.chatsdk.internal.NfConfigBuilder;
import com.nowfloats.chatsdk.internal.NfCoreFactory;
import com.nowfloats.chatsdk.internal.database.MessageRepository;
import com.nowfloats.chatsdk.internal.model.Content;
import com.nowfloats.chatsdk.internal.model.Data;
import com.nowfloats.chatsdk.internal.model.Message;
import com.nowfloats.chatsdk.internal.model.MessageResponse;
import com.nowfloats.chatsdk.internal.model.inputdata.Input;
import com.nowfloats.chatsdk.internal.network.ApiCalls;
import com.nowfloats.chatsdk.internal.utils.concurrent.PushConsumer;
import com.nowfloats.chatsdk.internal.utils.constants.Constants;

public final class NfCore {

    public static ConfigBuilder config() {
        return new NfConfigBuilder();
    }

    public static void install(NfChatSDKConfig config,
                               MessageListener listener) {
        NfCoreFactory.create(config, listener);
    }

    public static Message getLastMessage(Context context) {
        try {
            return MessageRepository.getInstance(context).getLastMessage().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateToken(Context context, String refreshedToken) {
        ApiCalls.updateToken(context, refreshedToken, null);
    }


    public static void handlePush(final Context context, final String payload) {
        try {
            MessageResponse messageResponse =
                    new Gson().fromJson(payload, MessageResponse.class);
            int messageType = messageResponse.getData().getType();
            messageResponse.getMessage().setMessageType(messageType);
            messageResponse.getMessage().setSyncWithServer(true);
            PushConsumer.getInstance(context).addTask(messageResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addWelcomeMessage(Context context) {
        MessageResponse.MessageResponseBuilder responseBuilder
                = new MessageResponse.MessageResponseBuilder(context);
        MessageResponse messageResponse = responseBuilder.build();
        Data data
                = new Data();
        data.setType(Constants.MessageType.INPUT);
        data.setContent(new Content());
        data.getContent().setInputType(Constants.InputType.TEXT);
        data.getContent().setMandatory(Constants.FCMConstants.MANDATORY_TRUE);
        Input input
                = new Input();
        input.setVal("Get Started");
        data.getContent().setInput(input);
        messageResponse.setData(data);
        int messageType = messageResponse.getData().getType();
        messageResponse.getMessage().setMessageType(messageType);
        messageResponse.getMessage().setSyncWithServer(false);
        messageResponse.getMessage().setTimestamp(System.currentTimeMillis());
        MessageRepository.getInstance(context).handleMessageResponse(messageResponse);
    }


}


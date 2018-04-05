package com.anachat.chatsdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anachat.chatsdk.internal.AnaConfigBuilder;
import com.anachat.chatsdk.internal.AnaCoreFactory;
import com.anachat.chatsdk.internal.database.MessageRepository;
import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.internal.model.Content;
import com.anachat.chatsdk.internal.model.Data;
import com.anachat.chatsdk.internal.model.Event;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.MessageResponse;
import com.anachat.chatsdk.internal.model.inputdata.DefaultLocation;
import com.anachat.chatsdk.internal.model.inputdata.Input;
import com.anachat.chatsdk.internal.network.ApiCalls;
import com.anachat.chatsdk.internal.utils.ListenerManager;
import com.anachat.chatsdk.internal.utils.concurrent.ApiExecutor;
import com.anachat.chatsdk.internal.utils.concurrent.ApiExecutorFactory;
import com.anachat.chatsdk.internal.utils.concurrent.PushConsumer;
import com.anachat.chatsdk.internal.utils.constants.Constants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class AnaCore {

    public static ConfigBuilder config() {
        return new AnaConfigBuilder();
    }

    public static void install(AnaChatSDKConfig config,
                               MessageListener listener) {
        AnaCoreFactory.create(config, listener);
    }

    public static Message getLastMessage(Context context) {
        try {
            return MessageRepository.getInstance(context).getLastMessage().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Message getLastSimpleMessage(Context context) {
        try {
            return MessageRepository.getInstance(context).getLastSimpleMessage().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void updateToken(Context context, String refreshedToken,
//                                   @NonNull String baseUrl, @NonNull String businessId,
//                                   String userId) {
//        PreferencesManager.getsInstance(context).setBusinessId(businessId);
//        PreferencesManager.getsInstance(context).setBaseUrl(baseUrl);
//        if (userId != null && !userId.isEmpty())
//            PreferencesManager.getsInstance(context).setUserNameLogin(userId);
//        ApiCalls.updateToken(context, refreshedToken, null);
//    }

    public static void saveFcmToken(@NonNull Context context, @NonNull String token) {
        if (!token.isEmpty()) {
            PreferencesManager.getsInstance(context).setFcmToken(token);
        }
    }


    public static void saveFcmToken(@NonNull Context context, @NonNull String token,
                                    @NonNull String username) {
        if (!token.isEmpty() && !username.isEmpty()) {
            PreferencesManager.getsInstance(context).setFcmToken(token);
            PreferencesManager.getsInstance(context).setUserNameLogin(username);
            if (PreferencesManager.getsInstance(context).getBusinessId().isEmpty() ||
                    PreferencesManager.getsInstance(context).getBaseUrl().isEmpty()) return;
            registerUser(context, username,
                    PreferencesManager.getsInstance(context).getBusinessId(),
                    PreferencesManager.getsInstance(context).getBaseUrl());
        }
    }


    public static void handlePush(final Context context, final String payload) {
        try {
            MessageResponse messageResponse =
                    new Gson().fromJson(payload, MessageResponse.class);
            if (messageResponse != null && messageResponse.getData() != null &&
                    messageResponse.getMessage() != null) {
                int messageType = messageResponse.getData().getType();
                messageResponse.getMessage().setMessageType(messageType);
                messageResponse.getMessage().setSyncWithServer(true);
                messageResponse.setNotifyMessage(true);
                PushConsumer.getInstance().addTask(messageResponse, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void disableCarousel(Context context) {
//        try {
//            MessageRepository.getInstance(context).
//                    updateCarouselMessage();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public static long getOldestTimeStamp(Context context) {
        try {
            return MessageRepository.getInstance(context).getFirstMessage().get(0).getTimestamp();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static void loadMoreMessages(Context context, int totalItems, int page, long timestamp) {
        ApiExecutor apiExecutor = ApiExecutorFactory.getHandlerExecutor();
        apiExecutor.runAsync(() -> {
            try {
                List<Message> messages =
                        MessageRepository.getInstance(context).loadHistoryMessages(totalItems);
                if (messages != null && messages.size() > 0) {
                    ListenerManager.getInstance().notifyHistoryLoaded(messages, page);
                } else {
                    ApiCalls.fetchHistoryMessages(context, page, totalItems, timestamp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void loadInitialHistory(Context context) {
        ApiCalls.fetchHistoryMessages(context, 0, 0, 0);
    }

    public static void addLocationPickListener(LocationPickListener locationPickListener) {
        ListenerManager.getInstance().addMessageChangeListener(locationPickListener);
    }

    public static void addCustomMethodListener(CustomMethodListener customMethodListener) {
        ListenerManager.getInstance().addCustomMethodListener(customMethodListener);
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
        if (!PreferencesManager.getsInstance(context).getEventsData().isEmpty()) {
            Event event
                    = new Event();
            event.setType(21);
            event.setData(PreferencesManager.getsInstance(context).getEventsData());
            List<Event> events = new ArrayList<>();
            events.add(event);
            messageResponse.setEvents(events);
        }
        if (!PreferencesManager.getsInstance(context).getFlowId().isEmpty()) {
            messageResponse.getMessage().setFlowId(PreferencesManager.getsInstance(context)
                    .getFlowId());
        }
        int messageType = messageResponse.getData().getType();
        messageResponse.getMessage().setMessageType(messageType);
        messageResponse.getMessage().setSyncWithServer(false);
        messageResponse.getMessage().setTimestamp(System.currentTimeMillis());
        MessageRepository.getInstance(context).handleMessageResponse(messageResponse);
    }


    public static void registerUser(@NonNull Context context,
                                    @NonNull String userId,
                                    @NonNull String businessId, @NonNull String baseUrl) {
        PreferencesManager.getsInstance(context).setBusinessId(businessId);
        PreferencesManager.getsInstance(context).setBaseUrl(baseUrl);
        PreferencesManager.getsInstance(context).setUserNameLogin(userId);
        ApiCalls.updateToken(context, null);
    }

    public static void logoutUser(@NonNull Context context) {
        String fcmToken = PreferencesManager.getsInstance(context).getFcmToken();
        PreferencesManager.getsInstance(context).clear();
        if (!fcmToken.isEmpty()) {
            PreferencesManager.getsInstance(context).setFcmToken(fcmToken);
        }
        MessageRepository.getInstance(context).clearTables();
    }

    public static void sendLocation(Double lat, Double lng, Context context) {
        Message message
                = getLastMessage(context);
        Input input = new Input();
        input.setLocation(new DefaultLocation(BigDecimal.valueOf(lat),
                BigDecimal.valueOf(lng)));
        MessageResponse.MessageResponseBuilder responseBuilder
                = new MessageResponse.MessageResponseBuilder(context);
        responseBuilder.
                inputLocation(message, input)
                .build().send();
    }

    public static void sendDeeplinkEventData(Context context, HashMap<String, String> eventsData, String title, String value, Boolean fromCarousel)
    {
        Message message
                = getLastMessage(context);
        if(fromCarousel) {
            Input input = new Input();
            input.setVal(value);
            input.setText(title);
            MessageResponse.MessageResponseBuilder responseBuilder
                    = new MessageResponse.MessageResponseBuilder(context);
            responseBuilder.
                    inputCarousel(input, message)
                    .build().send();
        }
        else {
            message.getMessageInput().setMandatory(Constants.FCMConstants.MANDATORY_TRUE);
            MessageResponse.MessageResponseBuilder responseBuilder
                    = new MessageResponse.MessageResponseBuilder
                    (context.getApplicationContext().getApplicationContext());
            MessageResponse messageResponse = responseBuilder.inputTextString(value,
                    message)
                    .build();
            if (eventsData != null || !eventsData.isEmpty()) {
                Event event
                        = new Event();
                event.setType(21);
                event.setData(new JSONObject(eventsData).toString());
                List<Event> events = new ArrayList<>();
                events.add(event);
                messageResponse.setEvents(events);
            }
            messageResponse.getData().getContent().getInput().setText(title);
            messageResponse.send();
        }
    }
}


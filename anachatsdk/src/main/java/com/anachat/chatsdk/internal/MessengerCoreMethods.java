package com.anachat.chatsdk.internal;

import android.content.Context;

import com.anachat.chatsdk.AnaChatSDKConfig;
import com.anachat.chatsdk.AnaCore;
import com.anachat.chatsdk.MessageListener;
import com.anachat.chatsdk.internal.database.MessageRepository;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.MessageResponse;
import com.anachat.chatsdk.internal.network.ApiCalls;
import com.anachat.chatsdk.internal.utils.ConnectionDetector;
import com.anachat.chatsdk.internal.utils.ListenerManager;
import com.anachat.chatsdk.internal.utils.NFChatSDK;
import com.anachat.chatsdk.internal.utils.NFChatUtils;
import com.anachat.chatsdk.internal.utils.concurrent.ApiExecutor;
import com.anachat.chatsdk.internal.utils.concurrent.ApiExecutorFactory;
import com.anachat.chatsdk.internal.utils.constants.Constants;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MessengerCoreMethods {
    private static final String TAG = "MessengerCoreMethods";
    private Context mContext;
    private AnaChatSDKConfig AnaChatSDKConfig;
    private MessageListener mListener;
    private ConnectionDetector cd;

    public MessengerCoreMethods(AnaChatSDKConfig AnaChatSDKConfig, MessageListener
            listener) throws IllegalArgumentException {

        this.AnaChatSDKConfig = AnaChatSDKConfig;
        this.mListener = listener;
        this.mContext = this.AnaChatSDKConfig.getContext();
        cd = new ConnectionDetector(mContext);
        if (this.mListener == null) {
            throw new IllegalArgumentException("Messenger listener cannot be null.");
        }
        NFChatSDK.instance().initialize(mContext);
        ListenerManager.getInstance().addMessageChangeListener(mListener);
        getAllMessages();
    }

    private void getAllMessages() {
        ApiExecutor apiExecutor = ApiExecutorFactory.getHandlerExecutor();
        apiExecutor.submitToPool(() -> {
            try {
                if (NFChatUtils.isNetworkConnected(mContext)) {
                    AnaCore.loadInitialHistory(mContext);
                    return;
                }
                final List<Message> messages
                        = MessageRepository.getInstance(mContext).getMessages();
                ApiExecutor executor = ApiExecutorFactory.getHandlerExecutor();
                executor.runOnUiThread(() -> mListener.onConversationUpdate(messages));
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void handleMessagePush(Message message, Context mContext) {
        MessageResponse messageResponse = null;
        if (message.getMessageType() == Constants.MessageType.INPUT) {
            messageResponse
                    = new MessageResponse.
                    MessageResponseBuilder(mContext).buildFromMessage(message).build();
        } else if (message.getMessageType() == Constants.MessageType.CAROUSEL) {
            messageResponse = new MessageResponse.
                    MessageResponseBuilder(mContext).buildCarousel(message).build();
        }
        if (messageResponse != null)
            ApiCalls.sendMessage(mContext.getApplicationContext(), messageResponse);
    }

    public static void syncMessages(Context context) {
        if (!NFChatUtils.isNetworkConnected(context)) return;
        ApiExecutor apiExecutor = ApiExecutorFactory.getHandlerExecutor();
        apiExecutor.runAsync(() -> {
            try {
                final List<Message> messages
                        = MessageRepository.getInstance(context).getUnSentMessages();
                for (Message message : messages) {
                    handleMessagePush(message, context);
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }
}

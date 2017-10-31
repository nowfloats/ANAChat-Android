package com.anachat.chatsdk.internal.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.anachat.chatsdk.MessageListener;
import com.anachat.chatsdk.internal.model.Message;

import java.util.HashSet;
import java.util.Set;

import static android.content.ContentValues.TAG;


public class ListenerManager {
    private Set<MessageListener> mChatMessageListeners = new HashSet<>();


    private static ListenerManager instance;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private ListenerManager() {
    }

    public static ListenerManager getInstance() {
        if (instance == null) { // first time lock
            synchronized (ListenerManager.class) {
                if (instance == null) {  // second time lock
                    instance = new ListenerManager();
                }
            }
        }
        return instance;
    }

    public void addMessageChangeListener(MessageListener messageListener) {
        mChatMessageListeners.add(messageListener);
    }

    public void removeChatMessageListener(MessageListener messageListener) {
        mChatMessageListeners.remove(messageListener);
    }


    public void notifyNewMessage(final Message message) {
        mHandler.post(new Runnable() {
            public void run() {
                if (message != null) {
                    for (MessageListener messageListener : mChatMessageListeners) {
                        messageListener.onMessageInserted(message);
                    }
                }
            }
        });
    }

    public void notifyMessageUpdate(final Message message) {
        mHandler.post(new Runnable() {
            public void run() {
                if (message != null) {
                    for (MessageListener messageListener : mChatMessageListeners) {
                        messageListener.onMessageUpdated(message);
                        Log.d(TAG, "notifyMessageUpdate with API!" + message.
                                getMessageId());
                    }
                }
            }
        });
    }

    public void notifyAddLoading() {
        mHandler.post(new Runnable() {
            public void run() {
                for (MessageListener messageListener : mChatMessageListeners) {
                    messageListener.addLoadingIndicator();
                }
            }
        });
    }

    public void notifyRemoveLoading() {
        mHandler.post(new Runnable() {
            public void run() {
                for (MessageListener messageListener : mChatMessageListeners) {
                    messageListener.removeLoadingIndicator();
                }
            }
        });
    }

    public void notifyMessageDelete(final Message message) {
        mHandler.post(new Runnable() {
            public void run() {
                if (message != null) {
                    for (MessageListener messageListener : mChatMessageListeners) {
                        messageListener.onMessageDeleted(message);
                    }
                }
            }
        });
    }
}

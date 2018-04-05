package com.anachat.chatsdk.internal.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.anachat.chatsdk.CustomMethodListener;
import com.anachat.chatsdk.LocationPickListener;
import com.anachat.chatsdk.MessageListener;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.utils.constants.Constants;
import com.anachat.chatsdk.uimodule.AnaChatActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;


public class ListenerManager {
    private Set<MessageListener> mChatMessageListeners = new HashSet<>();
    private Set<LocationPickListener> mLocationListeners = new HashSet<>();
    private Set<CustomMethodListener> mCustomMethodListeners = new HashSet<>();


    private static volatile ListenerManager instance;
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


    public void addMessageChangeListener(LocationPickListener messageListener) {
        mLocationListeners.add(messageListener);
    }

    public void addCustomMethodListener(CustomMethodListener customMethodListener) {
        mCustomMethodListeners.add(customMethodListener);
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

    public void notifyMessageUpdate(final Message message, long oldTime) {
        mHandler.post(new Runnable() {
            public void run() {
                if (message != null) {
                    for (MessageListener messageListener : mChatMessageListeners) {
                        messageListener.onMessageUpdated(message, oldTime);
                        Log.d(TAG, "notifyMessageUpdate with API!" + message.
                                getMessageId());
                    }
                }
            }
        });
    }

    public void notifyHistoryLoaded(final List<Message> message, Integer page) {
        mHandler.post(() -> {
            if (message != null) {
                for (MessageListener messageListener : mChatMessageListeners) {
                    if (page != 0)
                        messageListener.onHistoryLoaded(message);
                    else
                        messageListener.onConversationUpdate(message);
                }
            }
        });
    }

    public void notifyTypingIndicator(Integer type) {
        mHandler.post(() -> {
            for (MessageListener messageListener : mChatMessageListeners) {
                messageListener.addTypingIndicator(type);
            }

        });
    }

    public void notifyMessageDelete(final Message message) {
        mHandler.post(() -> {
            if (message != null) {
                for (MessageListener messageListener : mChatMessageListeners) {
                    messageListener.onMessageDeleted(message);
                }
            }
        });
    }

    public Intent notifyPickLocation(AnaChatActivity activity) {
        mHandler.post(() -> {
            for (LocationPickListener messageListener : mLocationListeners) {
                final Intent intent = messageListener.pickLocation(activity);
                if (intent != null)
                    activity.startActivityForResult(intent, Constants.InputType.LOCATION);
            }
        });
        return null;
    }

    public void notifySendLocation(Intent intent) {
        mHandler.post(() -> {
            for (LocationPickListener messageListener : mLocationListeners) {
                messageListener.sendLocation(intent);
            }
        });
    }

    public void callCustomMethod(Context context, String url, String title, String value) {
            for (CustomMethodListener methodListener : mCustomMethodListeners) {
                methodListener.implementCustomMethod(context, url, title, value);
            }
    }
}

package com.nowfloats.chatsdk;

import com.nowfloats.chatsdk.internal.model.Message;

import java.util.List;

public interface MessageListener {
    void onMessageUpdated(Message message);

    void onMessageInserted(Message message);

    void onMessageDeleted(Message message);

    void onConversationUpdate(List<Message> messages);

    void addLoadingIndicator();

    void removeLoadingIndicator();
}


package com.nowfloats.chatsdk.uimodule.viewholder;

import android.view.View;

import com.nowfloats.chatsdk.internal.model.Message;
import com.nowfloats.chatsdk.uimodule.chatuikit.messages.MessageHolders;

/**
 * Created by lookup on 30/09/17.
 */


public class BlankMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {
    public BlankMessageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
    }
}


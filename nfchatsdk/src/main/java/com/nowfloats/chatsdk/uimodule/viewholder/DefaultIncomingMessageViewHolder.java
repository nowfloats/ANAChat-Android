package com.nowfloats.chatsdk.uimodule.viewholder;

import android.view.View;
import android.widget.TextView;

import com.nowfloats.chatsdk.internal.model.Message;
import com.nowfloats.chatsdk.library.R;
import com.nowfloats.chatsdk.uimodule.chatuikit.messages.MessageHolders;
import com.nowfloats.chatsdk.uimodule.chatuikit.utils.DateFormatter;



public class DefaultIncomingMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private TextView tvDuration;
    private TextView tvTime;

    public DefaultIncomingMessageViewHolder(View itemView) {
        super(itemView);
        tvDuration = itemView.findViewById(R.id.messageText);
        tvTime = itemView.findViewById(R.id.messageTime);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        tvDuration.setText(message.getMessageInput().getInputTypeText().getInput().getInput());
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }
}

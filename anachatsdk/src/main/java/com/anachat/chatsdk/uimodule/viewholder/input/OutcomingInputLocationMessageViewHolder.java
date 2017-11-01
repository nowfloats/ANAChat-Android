package com.anachat.chatsdk.uimodule.viewholder.input;

import android.view.View;
import android.widget.TextView;

import com.anachat.chatsdk.uimodule.chatuikit.utils.DateFormatter;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.utils.constants.Constants;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessageHolders;


public class OutcomingInputLocationMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    private TextView tvDuration;
    private TextView tvTime;

    public OutcomingInputLocationMessageViewHolder(View itemView) {
        super(itemView);
        tvDuration = itemView.findViewById(R.id.messageText);
        tvTime = itemView.findViewById(R.id.messageTime);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        if (message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE) {
            tvDuration.setText(message.getMessageInput().getInputTypeLocation().
                    getDefaultLocation().getId() + "default_loc");
        } else {
            tvDuration.setText(message.getMessageInput().getInputTypeLocation().
                    getDefaultLocation().getId() + "default_loc");
        }
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }
}

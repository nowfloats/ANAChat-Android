package com.nowfloats.chatsdk.uimodule.viewholder.carousel;

import android.view.View;
import android.widget.TextView;

import com.nowfloats.chatsdk.internal.model.Message;
import com.nowfloats.chatsdk.library.R;
import com.nowfloats.chatsdk.uimodule.chatuikit.messages.MessageHolders;
import com.nowfloats.chatsdk.uimodule.chatuikit.utils.DateFormatter;


public class OutComingCarouselTextMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    private TextView tvText;
    private TextView tvTime;

    public OutComingCarouselTextMessageViewHolder(View itemView) {
        super(itemView);
        tvText = itemView.findViewById(R.id.messageText);
        tvTime = itemView.findViewById(R.id.messageTime);

    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        if (!imageLoader.isPreviousMessageHasSameAuthor(message.getUserId(), getAdapterPosition())) {
            triangle.setVisibility(View.GONE);
        } else {
            triangle.setVisibility(View.VISIBLE);
        }
        String text = message.getMessageCarousel().getInput().getOptionText();
        tvText.setText(text);
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }
}

package com.anachat.chatsdk.uimodule.viewholder.input;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anachat.chatsdk.internal.model.Media;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessageHolders;
import com.anachat.chatsdk.uimodule.chatuikit.utils.DateFormatter;

public class OutcomingInputMediaMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    private ImageView ivPreviewMedia;
    private TextView tvTime;
    private TextView tvType;
    private ImageView ivSentStatus;

    public OutcomingInputMediaMessageViewHolder(View itemView) {
        super(itemView);
        ivPreviewMedia = itemView.findViewById(R.id.image);
        tvTime = itemView.findViewById(R.id.messageTime);
        tvType = itemView.findViewById(R.id.message_type);
        ivSentStatus = itemView.findViewById(R.id.iv_sent_status);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        ivSentStatus.setColorFilter(Color.WHITE);
        if (message.getSyncWithServer()) {
            ivSentStatus.setImageDrawable
                    (ContextCompat.getDrawable(imageLoader.getContext(), R.drawable.ic_tick));
        } else {
            ivSentStatus.setImageDrawable
                    (ContextCompat.getDrawable(imageLoader.getContext(), R.drawable.ic_wait));
        }
        if (!imageLoader.isPreviousMessageHasSameAuthor(message.getUserId(), getAdapterPosition())) {
            triangle.setVisibility(View.GONE);
        } else {
            triangle.setVisibility(View.VISIBLE);
        }
        Media media = message.getMessageInput().
                getInputTypeMedia().getInput().getMedia().get(0);
        imageLoader.loadImage(ivPreviewMedia, media.getUrl());
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        switch (media.getType()) {
            case 2:
                tvType.setText("VIDEO");
                break;
            case 1:
                break;
            case 0:
                tvType.setText("PHOTO");
                break;
        }
        itemView.setOnClickListener(view -> {
            imageLoader.openMedia(media.getUrl(), media.getType());

        });
    }
}

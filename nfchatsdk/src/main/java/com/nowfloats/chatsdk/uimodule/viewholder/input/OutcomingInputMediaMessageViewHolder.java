package com.nowfloats.chatsdk.uimodule.viewholder.input;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowfloats.chatsdk.internal.model.Media;
import com.nowfloats.chatsdk.internal.model.Message;
import com.nowfloats.chatsdk.library.R;
import com.nowfloats.chatsdk.uimodule.chatuikit.messages.MessageHolders;
import com.nowfloats.chatsdk.uimodule.chatuikit.utils.DateFormatter;

public class OutcomingInputMediaMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    private ImageView ivPreviewMedia;
    private TextView tvTime;
    private TextView tvType;

    public OutcomingInputMediaMessageViewHolder(View itemView) {
        super(itemView);
        ivPreviewMedia = itemView.findViewById(R.id.image);
        tvTime = itemView.findViewById(R.id.messageTime);
        tvType = itemView.findViewById(R.id.message_type);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
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

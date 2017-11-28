package com.anachat.chatsdk.uimodule.viewholder.input;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.inputdata.DefaultLocation;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessageHolders;
import com.anachat.chatsdk.uimodule.chatuikit.utils.DateFormatter;


public class OutcomingInputLocationMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    private ImageView ivPreviewMedia;
    private TextView tvTime;
    private TextView tvType;

    public OutcomingInputLocationMessageViewHolder(View itemView) {
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
        DefaultLocation latLng = message.getMessageInput().getInputTypeLocation()
                .getInput().getLocation();
        String URL = "http://maps.google.com/maps/api/staticmap?center=" +
                latLng.getLat() + "," + latLng.getLng() + "&zoom=15&size=200x200&sensor=false";
        imageLoader.loadImage(ivPreviewMedia, URL);
        tvType.setText(imageLoader.getContext().getString(R.string.location));
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));

        itemView.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(URL));
                imageLoader.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}

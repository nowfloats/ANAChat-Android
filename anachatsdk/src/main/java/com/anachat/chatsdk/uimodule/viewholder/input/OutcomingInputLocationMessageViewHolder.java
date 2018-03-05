package com.anachat.chatsdk.uimodule.viewholder.input;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anachat.chatsdk.internal.database.PreferencesManager;
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
    private ImageView ivSentStatus;
    private ProgressBar progressBar;

    public OutcomingInputLocationMessageViewHolder(View itemView) {
        super(itemView);
        ivPreviewMedia = itemView.findViewById(R.id.image);
        tvTime = itemView.findViewById(R.id.messageTime);
        tvType = itemView.findViewById(R.id.message_type);
        ivSentStatus = itemView.findViewById(R.id.iv_sent_status);
        progressBar = itemView.findViewById(R.id.pv_loader);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, Color.parseColor
                    (PreferencesManager.getsInstance(imageLoader.getContext()).getThemeColor()));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor
                            (PreferencesManager.getsInstance(imageLoader.getContext()).getThemeColor()),
                    PorterDuff.Mode.SRC_IN);
        }
        ivSentStatus.setAlpha(1f);
        ivSentStatus.setColorFilter(ContextCompat.getColor(imageLoader.getContext(), R.color.white));
        if (message.getSyncWithServer()) {
            progressBar.setVisibility(View.GONE);
            ivSentStatus.setImageDrawable
                    (ContextCompat.getDrawable(imageLoader.getContext(), R.drawable.ic_tick));
        } else {
            progressBar.setVisibility(View.VISIBLE);
            ivSentStatus.setImageDrawable
                    (ContextCompat.getDrawable(imageLoader.getContext(), R.drawable.ic_wait));
        }
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

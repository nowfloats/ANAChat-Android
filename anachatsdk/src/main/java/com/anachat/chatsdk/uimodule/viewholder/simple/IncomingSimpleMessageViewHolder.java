package com.anachat.chatsdk.uimodule.viewholder.simple;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessageHolders;
import com.anachat.chatsdk.uimodule.chatuikit.utils.DateFormatter;


public class IncomingSimpleMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private TextView tvText;
    private TextView tvTime;

    public IncomingSimpleMessageViewHolder(View itemView) {
        super(itemView);
        tvText = itemView.findViewById(R.id.messageText);
        tvTime = itemView.findViewById(R.id.messageTime);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(imageLoader.getContext(),
                R.drawable.bg_chat_buuble_incoming);
        GradientDrawable gradientDrawable = (GradientDrawable) shape
                .findDrawableByLayerId(R.id.red_line);
        gradientDrawable.setColor(Color.parseColor
                (PreferencesManager.getsInstance(imageLoader.getContext()).getThemeColor()));
        ViewCompat.setBackground(bubble, shape);
        triangle.setColorFilter(Color.parseColor
                (PreferencesManager.getsInstance(imageLoader.getContext()).getThemeColor()));
        if (!imageLoader.isPreviousMessageHasSameAuthor
                (message.getFrom().getId(), getAdapterPosition())) {
            triangle.setVisibility(View.GONE);
        } else {
            triangle.setVisibility(View.VISIBLE);
        }
        if (message.getMessageSimple().getText() != null &&
                !message.getMessageSimple().getText().trim().isEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvText.setText(Html.fromHtml(message.getMessageSimple().getText().trim(),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvText.setText(Html.fromHtml(message.getMessageSimple().getText().trim()));
            }
        } else {
            tvText.setText(message.getMessageSimple().getText().trim());
        }
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }
}

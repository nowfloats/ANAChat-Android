package com.anachat.chatsdk.uimodule.viewholder.carousel;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anachat.chatsdk.internal.model.Message;
import com.nowfloats.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessageHolders;
import com.anachat.chatsdk.uimodule.ui.adapter.ItemsAdapter;


public class IncomingCarouselMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private RecyclerView rvCarousel;

    public IncomingCarouselMessageViewHolder(View itemView) {
        super(itemView);
        rvCarousel = itemView.findViewById(R.id.rv_carousel);

    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        rvCarousel.setLayoutManager(new LinearLayoutManager(imageLoader.getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        rvCarousel.setAdapter(new ItemsAdapter(message
                , imageLoader,
                message.getMessageCarousel().getEnabled()));
    }
}

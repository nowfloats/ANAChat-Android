package com.anachat.chatsdk.uimodule.ui.adapter;

/**
 * Created by lookup on 11/10/17.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anachat.chatsdk.internal.model.Item;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.commons.ImageLoader;
import com.anachat.chatsdk.uimodule.chatuikit.utils.RoundishImageView;
import com.anachat.chatsdk.uimodule.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private List<Item> itemList;
    private ImageLoader imageLoader;
    private Message message;
    private Boolean enabled;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTittle, tvDesc;
        public RoundishImageView ivItem;
        public RecyclerView rvOptions;
        public LinearLayout llText;
        public LinearLayout llBottom;

        public MyViewHolder(View view) {
            super(view);
            tvTittle = view.findViewById(R.id.tv_title);
            tvDesc = view.findViewById(R.id.tv_desc);
            rvOptions = view.findViewById(R.id.rv_options);
            ivItem = view.findViewById(R.id.iv_item);
            llText = view.findViewById(R.id.ll_text);
            llBottom = view.findViewById(R.id.linear_layout_bottom);
        }
    }


    public ItemsAdapter(Message message, ImageLoader imageLoader, Boolean enabled) {
        this.itemList = new ArrayList<>(message.getMessageCarousel().getItems());
        this.imageLoader = imageLoader;
        this.message = message;
        this.enabled = enabled;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carousel_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        if (item.getTitle().isEmpty() && item.getDesc().isEmpty()) {
            holder.ivItem.getLayoutParams().height = AppUtils.dpToPx(247);
            holder.llText.setVisibility(View.GONE);
        }
        holder.tvTittle.setText(item.getTitle());
        holder.tvDesc.setText(item.getDesc());
        holder.rvOptions.setLayoutManager(new LinearLayoutManager(
                imageLoader.getContext(), LinearLayoutManager.VERTICAL, false));
        holder.rvOptions.setAdapter(new OptionsAdapterCarouselItem(imageLoader,
                message, item, enabled));
        imageLoader.loadImage(holder.ivItem, item.getMedia().getUrl());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
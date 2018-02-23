package com.anachat.chatsdk.uimodule.ui.adapter;

/**
 * Created by lookup on 11/10/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anachat.chatsdk.internal.model.Item;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.commons.ImageLoader;
import com.anachat.chatsdk.uimodule.chatuikit.utils.RoundishImageView;
import com.anachat.chatsdk.uimodule.utils.AppUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private List<Item> itemList;
    private ImageLoader imageLoader;
    private Message message;
    private Boolean enabled;
    private Integer maxHeight = 200;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTittle, tvDesc;
        public RoundishImageView ivItem;
        public RecyclerView rvOptions;
        public LinearLayout llText;
        public LinearLayout llBottom;
        public RelativeLayout rlRoot;

        public MyViewHolder(View view) {
            super(view);
            tvTittle = view.findViewById(R.id.tv_title);
            tvDesc = view.findViewById(R.id.tv_desc);
            rvOptions = view.findViewById(R.id.rv_options);
            ivItem = view.findViewById(R.id.iv_item);
            llText = view.findViewById(R.id.ll_text);
            llBottom = view.findViewById(R.id.linear_layout_bottom);
            rlRoot = view.findViewById(R.id.rl_root);
        }
    }


    public ItemsAdapter(Message message, ImageLoader imageLoader, Boolean enabled) {
        this.itemList = new ArrayList<>(message.getMessageCarousel().getItems());
        this.imageLoader = imageLoader;
        this.message = message;
        this.enabled = enabled;
        itemList.get(0).setDesc("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        try {
            maxHeight = getMaxHeight(itemList, imageLoader.getContext());
        } catch (Exception e) {
            maxHeight = AppUtils.dpToPx(120);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carousel_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvDesc.getLayoutParams().height = maxHeight;
//        holder.tvDesc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight));

        Item item = itemList.get(position);
//        if (item.getTitle().isEmpty() && item.getDesc().isEmpty()) {
//            holder.ivItem.getLayoutParams().height = AppUtils.dpToPx(247);
//            holder.llText.setVisibility(View.GONE);
//        }

        if (item.getTitle() != null &&
                !item.getTitle().trim().isEmpty()) {
            String text = item.getTitle().trim().
                    replaceAll("\\n?\n", "<br>");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.tvTittle.setText(Html.fromHtml(text.trim(),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.tvTittle.setText(Html.fromHtml(text.trim()));
            }
        } else {
            holder.tvTittle.setText(item.getTitle());
        }

        if (item.getDesc() != null &&
                !item.getDesc().trim().isEmpty()) {
            String text = item.getDesc().trim().
                    replaceAll("\\n?\n", "<br>");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.tvDesc.setText(Html.fromHtml(text.trim(),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.tvDesc.setText(Html.fromHtml(text.trim()));
            }
        } else {
            holder.tvDesc.setText(item.getDesc());
        }
        holder.rvOptions.setLayoutManager(new LinearLayoutManager(
                imageLoader.getContext(), LinearLayoutManager.VERTICAL, false));
        holder.rvOptions.setAdapter(new OptionsAdapterCarouselItem(imageLoader,
                message, item, enabled));
        if (item.getMedia() == null || item.getMedia().getUrl() == null
                || item.getMedia().getUrl().isEmpty()) {
            holder.ivItem.setVisibility(View.GONE);
//            holder.rlRoot.getLayoutParams().height = AppUtils.dpToPx(208);
        } else {
            holder.ivItem.setVisibility(View.VISIBLE);
            imageLoader.loadImage(holder.ivItem, item.getMedia().getUrl());
//            holder.rlRoot.getLayoutParams().height = AppUtils.dpToPx(360);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static int getMaxHeight(
            List<Item> itemList, Context context
    ) {
        Item maxValue = itemList.stream().max(Comparator.comparing(v -> v.getDesc().length())).get();
        // this Point already uses pixels
        final int deviceWidth = AppUtils.dpToPx(280);
        final int textSize = context.getResources().getDimensionPixelSize(R.dimen.message_time_text_size);
        return method2UsingTextViewAndMeasureSpec
                (context, maxValue.getDesc(), textSize, deviceWidth, AppUtils.dpToPx(8));
    }

    public static int method2UsingTextViewAndMeasureSpec(
            final Context context,
            final CharSequence text,
            final int textSize, // in pixels
            final int deviceWidth, // in pixels
            final int padding // in pixels
    ) {

        TextView textView = new TextView(context);
        textView.setPadding(padding, padding, padding, padding); // optional apply paddings here
        textView.setTypeface(Typeface.DEFAULT);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }
}
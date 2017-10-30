package com.nowfloats.chatsdk.uimodule.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nowfloats.chatsdk.internal.database.PreferencesManager;
import com.nowfloats.chatsdk.internal.model.Item;
import com.nowfloats.chatsdk.internal.model.Message;
import com.nowfloats.chatsdk.internal.model.MessageResponse;
import com.nowfloats.chatsdk.internal.model.Option;
import com.nowfloats.chatsdk.internal.model.inputdata.Input;
import com.nowfloats.chatsdk.library.R;
import com.nowfloats.chatsdk.uimodule.utils.InputIntents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lookup on 12/10/17.
 */


public class OptionsAdapterCarouselItem extends RecyclerView.Adapter<OptionsAdapterCarouselItem.MyViewHolder> {

    private List<Option> optionList;
    private Context context;
    private Message message;
    private Item item;
    private Boolean enableButtons = true;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_title);
        }
    }


    public OptionsAdapterCarouselItem(Context context, Message message, Item item, Boolean enableButtons) {
        this.optionList = new ArrayList<>(item.getOptionsForeignCollection());
        this.context = context;
        this.message = message;
        this.item = item;
        this.enableButtons = enableButtons;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carousel_options, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (!enableButtons) {
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.grey_300));
        } else {
            holder.title.setTextColor(Color.parseColor
                    (PreferencesManager.getsInstance(context).getThemeColor()));
        }
        final Option option = optionList.get(position);
        holder.title.setText(option.getTitle());
        holder.itemView.setOnClickListener(view -> {
            if (enableButtons) {
                String value = option.getValue();
                if (option.getType() == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(option.getValue());
                        if (jsonObject.has("url")) {
                            value = jsonObject.getString("value");
                            context.startActivity(
                                    InputIntents.getBrowserIntent(jsonObject.getString("url")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Input input = new Input();
                input.setVal(value);
                input.setOptionText(option.getTitle());
                MessageResponse.MessageResponseBuilder responseBuilder
                        = new MessageResponse.MessageResponseBuilder(context);
                responseBuilder.
                        inputCarousel(input, message)
                        .build().send();
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }
}
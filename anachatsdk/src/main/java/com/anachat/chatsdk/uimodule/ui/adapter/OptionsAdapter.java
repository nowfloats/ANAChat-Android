package com.anachat.chatsdk.uimodule.ui.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.internal.model.Event;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.MessageResponse;
import com.anachat.chatsdk.internal.model.Option;
import com.anachat.chatsdk.internal.utils.ListenerManager;
import com.anachat.chatsdk.internal.utils.constants.Constants;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.commons.ImageLoader;
import com.anachat.chatsdk.uimodule.utils.InputIntents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {

    private List<Option> optionList;
    private Context context;
    private Context mContext;
    private Message message;
    private ImageLoader imageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.messageText);
        }
    }


    public OptionsAdapter(ImageLoader imageLoader, Context context) {
        this.context = imageLoader.getContext();
        this.mContext = context;
        this.imageLoader = imageLoader;
        optionList = new ArrayList<>();
    }

    public void setData(Message message) {
        clear();
        this.message = message;
        this.optionList = message.getMessageInput().getOptionsAsList();
        notifyDataSetChanged();
    }

    private void clear() {
        optionList.clear();
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_input_type_options, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        GradientDrawable drawable = (GradientDrawable) holder.itemView.getBackground();
        drawable.setColor(Color.parseColor(PreferencesManager.getsInstance(context).getThemeColor()));
        drawable.setStroke(3,
                Color.parseColor(PreferencesManager.getsInstance(context).getThemeColor()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.itemView.setBackground(drawable);
        }else{
            holder.itemView.setBackgroundDrawable(drawable);
        }
        final Option option = optionList.get(position);
        if (option.getTitle() != null &&
                !option.getTitle().trim().isEmpty()) {
            String text = option.getTitle().trim().
                    replaceAll("\\n?\n", "<br>");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.title.setText(Html.fromHtml(text.trim(),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.title.setText(Html.fromHtml(text.trim()));
            }
        } else {
            holder.title.setText(option.getTitle());
        }

        holder.title.setTextColor(PreferencesManager.getsInstance(holder.title.getContext())
        .getOptionsTextColor());

        holder.itemView.setOnClickListener(view -> {
            String value = option.getValue();
            if (option.getType() == 0) {
                try {
                    JSONObject jsonObject = new JSONObject(option.getValue());
                    if (jsonObject.has("url")) {
                        value = jsonObject.getString("value");
                        context.startActivity(
                                InputIntents.getBrowserIntent(imageLoader.getContext(),
                                        jsonObject.getString("url")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (option.getType() == 3) {
                try {
                    JSONObject jsonObject = new JSONObject(option.getValue());
                    if (jsonObject.has("url")) {
                        value = jsonObject.getString("value");
                        ListenerManager.getInstance().callCustomMethod(mContext, jsonObject.getString("url"), option.getTitle(), value);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(option.getType() != 3) {
                message.getMessageInput().setMandatory(Constants.FCMConstants.MANDATORY_TRUE);
                MessageResponse.MessageResponseBuilder responseBuilder
                        = new MessageResponse.MessageResponseBuilder
                        (context.getApplicationContext().getApplicationContext());
                MessageResponse messageResponse = responseBuilder.inputTextString(value,
                        message)
                        .build();
                messageResponse.getData().getContent().getInput().setText(option.getTitle());
                messageResponse.send();
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }
}
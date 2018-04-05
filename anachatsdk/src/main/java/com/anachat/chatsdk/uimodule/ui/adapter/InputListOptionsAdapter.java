package com.anachat.chatsdk.uimodule.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.Option;
import com.anachat.chatsdk.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lookup on 14/11/17.
 */


public class InputListOptionsAdapter extends RecyclerView.Adapter<InputListOptionsAdapter.MyViewHolder> {

    private List<Option> optionList = new ArrayList<Option>();
    private Boolean multipleSelection = false;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView ivMark;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_title);
            ivMark = view.findViewById(R.id.iv_check_mark);
        }
    }


    public InputListOptionsAdapter(Context context, Message message, String searchText) {
        if(TextUtils.isEmpty(searchText))
            this.optionList = message.getMessageInput().getInputTypeList().getValuesAsList();
        else {
            for(Option option: message.getMessageInput().getInputTypeList().getValuesAsList())
            {
                if(option.getText().toUpperCase().contains(searchText.toUpperCase()))
                    this.optionList.add(option);
            }
        }
        this.context = context;
        if (message.getMessageInput().getInputTypeList().getMultiple() != null)
            this.multipleSelection = message.getMessageInput().getInputTypeList().getMultiple();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_input_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Option option = optionList.get(position);
        holder.title.setText(option.getText());
        holder.itemView.setOnClickListener(view -> {
            if (!multipleSelection) {
                deSelectAll();
            }
            if (!option.getSelected())
                option.setSelected(true);
            else option.setSelected(false);
            notifyDataSetChanged();
        });
        if (option.getSelected()) {
            holder.ivMark.setColorFilter(Color.parseColor
                    (PreferencesManager.getsInstance(context).getThemeColor()));
        } else {
            holder.ivMark.setColorFilter(ContextCompat.getColor(context,
                    R.color.grey_400));
        }
    }

    public String getValues() {
        StringBuilder result = new StringBuilder();
        for (Option option
                : optionList) {
            if (option.getSelected()) {
                result.append(option.getValue());
                result.append(",");
            }
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    private void deSelectAll() {
        for (Option option
                : optionList) {
            option.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }
}
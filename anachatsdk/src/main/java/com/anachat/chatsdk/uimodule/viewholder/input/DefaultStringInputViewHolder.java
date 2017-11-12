package com.anachat.chatsdk.uimodule.viewholder.input;

import android.view.View;
import android.widget.TextView;

import com.anachat.chatsdk.uimodule.chatuikit.utils.DateFormatter;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.inputdata.Address;
import com.anachat.chatsdk.internal.model.inputdata.Date;
import com.anachat.chatsdk.internal.model.inputdata.Time;
import com.anachat.chatsdk.internal.utils.constants.Constants;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessageHolders;


public class DefaultStringInputViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {
    private TextView tvText;
    private TextView tvTime;


    public DefaultStringInputViewHolder(View itemView) {
        super(itemView);
        tvText = itemView.findViewById(R.id.messageText);
        tvTime = itemView.findViewById(R.id.messageTime);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        if (!imageLoader.isPreviousMessageHasSameAuthor(message.getUserId(), getAdapterPosition())) {
            triangle.setVisibility(View.GONE);
        } else {
            triangle.setVisibility(View.VISIBLE);
        }
        String text = "";
        if (message.getMessageType() == Constants.MessageType.INPUT) {
            switch (message.getMessageInput().getInputType()) {

                case Constants.InputType.DATE:
                    Date date = message.getMessageInput().getInputTypeDate().getInput().getDate();
                    text = date.getMday() + "-" + date.getMonth() + "-" + date.getYear();
                    break;
                case Constants.InputType.TIME:
                    Time time = message.getMessageInput().getInputTypeTime().getInput().getTime();
                    text = time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
                    break;
                case Constants.InputType.ADDRESS:
                    Address address
                            = message.getMessageInput().getInputTypeAddress().getInput().getAddress();
                    text = address.getLine1() + " " + address.getArea() + "\n" + address.getCity() + " "
                            + address.getState() + " " + address.getCountry() + " "
                            + address.getPin();
                    break;
                case Constants.InputType.TEXT:
                    if (message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE)
                        text = message.getMessageInput().getInputTypeText().getInput().getVal();
                    else
                        text = message.getMessageInput().getInputTypeText().getInput().getInput();
                    break;
                case Constants.InputType.OPTIONS:
                    if (message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE)
                        text = message.getMessageInput().getInputForOptions().getText();
                    else
                        text = message.getMessageInput().getInputForOptions().getInput();
                    break;
            }
        }
        if (message.getMessageType() == Constants.MessageType.CAROUSEL) {
            text = message.getMessageCarousel().getInput().getText();
        }
        tvText.setText(text);
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));

    }
}

package com.anachat.chatsdk.internal.model;

import android.content.Context;

import com.anachat.chatsdk.internal.database.MessageRepository;
import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.internal.model.inputdata.Input;
import com.anachat.chatsdk.internal.model.inputdata.Participant;
import com.anachat.chatsdk.internal.utils.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class MessageResponse extends BaseModel {
    @SerializedName("meta")
    private Message message;
    @SerializedName("data")
    private Data data;
    private List<MessageResponse> chats;
    private transient Context context;
    private transient boolean onlyUpdate = false;
    private transient boolean fileUpload = false;
    private transient boolean notifyMessage = true;
    private transient long timestampToUpdate;

    public boolean isNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(boolean notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    private MessageResponse(MessageResponseBuilder messageResponseBuilder) {
        this.message = messageResponseBuilder.message;
        this.data = messageResponseBuilder.data;
        this.context = messageResponseBuilder.mContext;
    }

    public static class MessageResponseBuilder {
        private Message message;
        private Data data;

        private transient Context mContext;

        public MessageResponseBuilder(Context context) {
            this.mContext = context;
            this.message = buildMessage();
            this.data = new Data();
            data.setContent(new Content());
        }

        public MessageResponseBuilder inputTextString(String value, Message message) {
            switch (message.getMessageInput().getInputType()) {
                case Constants.InputType.TEXT:
                    return inputText(value, message);
                case Constants.InputType.EMAIL:
                    return inputEmail(value, message);
                case Constants.InputType.PHONE:
                    return inputPhone(value, message);
                case Constants.InputType.NUMERIC:
                    return inputNumeric(value, message);
                case Constants.InputType.OPTIONS:
                    return inputOption(value, message);
                case Constants.InputType.LIST:
                    return inputListOption(value, message);
            }
            return this;
        }

        private MessageResponseBuilder inputText(String value, Message message) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.TEXT);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setInput(buildInput(value, message.getMessageInput().getMandatory()));
            data.getContent().setTextInputAttr(
                    message.getMessageInput().getInputTypeText().getTextInputAttr());
            return this;
        }

        private MessageResponseBuilder inputEmail(String value, Message message) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.EMAIL);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setInput(buildInput(value, message.getMessageInput().getMandatory()));
            return this;
        }

        private MessageResponseBuilder inputPhone(String value, Message message) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.PHONE);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setInput(buildInput(value, message.getMessageInput().getMandatory()));
            return this;
        }

        private MessageResponseBuilder inputNumeric(String value, Message message) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.NUMERIC);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setInput(buildInput(value, message.getMessageInput().getMandatory()));
            return this;
        }

        public MessageResponseBuilder inputOption(String value, Message message) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.OPTIONS);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setOptions(message.getMessageInput().getOptionsAsList());
            data.getContent().setInput(buildInput(value, message.getMessageInput().getMandatory()));
            return this;
        }

        public MessageResponseBuilder inputListOption(String value, Message message) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.LIST);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setValues(message.getMessageInput().getInputTypeList().getValuesAsList());
            data.getContent().setInput(buildInput(value, message.getMessageInput().getMandatory()));
            return this;
        }


        public MessageResponseBuilder inputCarousel(Input value, Message message) {
            message.getMessageCarousel().setEnabled(false);
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.CAROUSEL);
            this.message.setMessageType(Constants.MessageType.CAROUSEL);
            for (Item item : message.getMessageCarousel().getItems()) {
                item.setOptions(new ArrayList<>(item.getOptionsForeignCollection()));
            }
            String itemsS = new Gson().toJson(message.getMessageCarousel().getItems());
            List<Item> items = new Gson().fromJson(itemsS,
                    new TypeToken<List<Item>>() {
                    }.getType());
            data.getContent().setMandatory(Constants.FCMConstants.MANDATORY_TRUE);
            data.getContent().setItems(items);
            data.getContent().setInput(value);
            return this;
        }

        public MessageResponseBuilder inputMedia(String messageResponseId, int mandatory
                , String sessionId) {
            this.message.setResponseTo(messageResponseId);
            this.message.setSessionId(sessionId);
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.MEDIA);
            data.getContent().setMandatory(mandatory);
//            data.getContent().setInput(buildMediaInput(path, mediaType));
            return this;
        }

        public MessageResponseBuilder inputDate(Message message, Input input) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.DATE);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setDateRange(message.getMessageInput().
                    getInputTypeDate().getDateRange());
            data.getContent().setInput(input);
            return this;
        }

        public MessageResponseBuilder inputTime(Message message, Input input) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.TIME);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setTimeRange(message.getMessageInput().
                    getInputTypeTime().getTimeRange());
            data.getContent().setInput(input);
            return this;
        }

        public MessageResponseBuilder inputAddress(Message message, Input input) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.ADDRESS);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            if (message.getMessageInput().getInputTypeAddress().getRequiredFields() != null)
                data.getContent().setRequiredFields(Arrays.asList(
                        message.getMessageInput().getInputTypeAddress().getRequiredFields()));
            data.getContent().setInput(input);
            return this;
        }

        public MessageResponseBuilder inputLocation(Message message, Input input) {
            this.message.setResponseTo(message.getMessageId());
            this.message.setSessionId(message.getSessionId());
            data.setType(Constants.MessageType.INPUT);
            this.message.setMessageType(Constants.MessageType.INPUT);
            data.getContent().setInputType(Constants.InputType.LOCATION);
            data.getContent().setMandatory(message.getMessageInput().getMandatory());
            data.getContent().setDefaultLocation(message.getMessageInput().
                    getInputTypeLocation().getDefaultLocation());
            data.getContent().setInput(input);
            return this;
        }

        public MessageResponse build() {
            return new MessageResponse(this);
        }

        public MessageResponseBuilder buildFromMessage(Message message) {
            this.data.setType(message.getMessageType());
            switch (message.getMessageInput().getInputType()) {
                case Constants.InputType.TEXT:
                    inputTextString(getValueFromInput(message.getMessageInput().getMandatory(),
                            message.getMessageInput().getInputTypeText().getInput()), message);
                    break;
                case Constants.InputType.NUMERIC:
                    inputTextString(getValueFromInput(message.getMessageInput().getMandatory(),
                            message.getMessageInput().getInputTypeNumeric().getInput()), message);
                    break;
                case Constants.InputType.EMAIL:
                    inputTextString(getValueFromInput(message.getMessageInput().getMandatory(),
                            message.getMessageInput().getInputTypeEmail().getInput()), message);
                    break;
                case Constants.InputType.PHONE:
                    inputTextString(getValueFromInput(message.getMessageInput().getMandatory(),
                            message.getMessageInput().getInputTypePhone().getInput()), message);
                    break;
                case Constants.InputType.ADDRESS:
                    inputAddress(message, message.getMessageInput().getInputTypeAddress().getInput());
                    break;
                case Constants.InputType.DATE:
                    inputAddress(message, message.getMessageInput().getInputTypeDate().getInput());
                    break;
                case Constants.InputType.TIME:
                    inputAddress(message, message.getMessageInput().getInputTypeTime().getInput());
                    break;
                case Constants.InputType.LOCATION:
                    break;
                case Constants.InputType.MEDIA:
                    Media input = message.getMessageInput().getInputTypeMedia().getInput().
                            getMedia().get(0);
                    buildMediaInput(input.getPreviewUrl(), input.getType());
                    inputMedia(message.getResponseTo(),
                            message.getMessageInput().getMandatory(), message.getSessionId());
                    break;
                case Constants.InputType.LIST:
                    break;
                case Constants.InputType.OPTIONS:
                    inputTextString(getValueFromInput(message.getMessageInput().getMandatory(),
                            message.getMessageInput().getInputForOptions()), message);
                    break;

            }

            this.message = message;
            return this;
        }

        public MessageResponseBuilder buildCarousel(Message message) {
            this.data.setType(message.getMessageType());
            inputCarousel(message.getMessageCarousel().getInput(), message);
            this.message = message;
            return this;
        }

        private String getValueFromInput(int mandatory, Input input) {
            if (mandatory == Constants.FCMConstants.MANDATORY_FALSE) {
                return input.getInput();
            } else {
                return input.getVal();
            }
        }

        private Input buildInput(String value, int mandatory) {
            Input input
                    = new Input();
            if (mandatory == Constants.FCMConstants.MANDATORY_FALSE) {
                input.setInput(value);
            } else {
                input.setVal(value);
            }
            return input;
        }

        public MessageResponseBuilder buildMediaInput(String path, int mediaType) {
            List<Media> medias = new ArrayList<>();
            Input input
                    = new Input();
            Media media
                    = new Media();
            media.setUrl(path);
            media.setPreviewUrl(path);
            media.setType(mediaType);
            medias.add(media);
            input.setMedia(medias);
            data.getContent().setInput(input);
            data.getContent().setMediaType(mediaType);
            return this;
        }

        private Message buildMessage() {
            Message message = new Message();
            message.setFrom(new Participant(PreferencesManager.getsInstance(mContext).getUserName(), 1));
            message.setTo(new Participant(PreferencesManager.getsInstance(mContext).getBusinessId(), 1));
            message.setSenderType(Constants.SenderType.USER);
//            message.setTimestamp(System.currentTimeMillis());
            message.setTimestamp(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis());
            return message;
        }
    }

    public void send() {
        MessageRepository.getInstance(context).handleMessageResponse(this);
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<MessageResponse> getChats() {
        return chats;
    }

    public void setChats(List<MessageResponse> chats) {
        this.chats = chats;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isOnlyUpdate() {
        return onlyUpdate;
    }

    public void setOnlyUpdate(boolean onlyUpdate) {
        this.onlyUpdate = onlyUpdate;
    }

    public boolean isFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(boolean fileUpload) {
        this.fileUpload = fileUpload;
    }

    public long getTimestampToUpdate() {
        return timestampToUpdate;
    }

    public void setTimestampToUpdate(long timestampToUpdate) {
        this.timestampToUpdate = timestampToUpdate;
    }
}

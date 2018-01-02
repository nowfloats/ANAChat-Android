package com.anachat.chatsdk.internal.database;

import android.content.Context;
import android.util.Log;

import com.anachat.chatsdk.internal.model.InputTypeAddress;
import com.anachat.chatsdk.internal.model.InputTypeDate;
import com.anachat.chatsdk.internal.model.InputTypeEmail;
import com.anachat.chatsdk.internal.model.InputTypeLocation;
import com.anachat.chatsdk.internal.model.InputTypeNumeric;
import com.anachat.chatsdk.internal.model.InputTypePhone;
import com.anachat.chatsdk.internal.model.InputTypeText;
import com.anachat.chatsdk.internal.model.InputTypeTime;
import com.anachat.chatsdk.internal.model.Item;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.MessageCarousel;
import com.anachat.chatsdk.internal.model.MessageInput;
import com.anachat.chatsdk.internal.model.MessageResponse;
import com.anachat.chatsdk.internal.model.MessageSimple;
import com.anachat.chatsdk.internal.model.Option;
import com.anachat.chatsdk.internal.model.inputdata.Input;
import com.anachat.chatsdk.internal.model.inputdata.InputTypeList;
import com.anachat.chatsdk.internal.model.inputdata.InputTypeMedia;
import com.anachat.chatsdk.internal.network.ApiCalls;
import com.anachat.chatsdk.internal.utils.ListenerManager;
import com.anachat.chatsdk.internal.utils.constants.Constants;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lookup on 06/09/17.
 */

public class MessageRepository {

    private static MessageRepository instance = null;
    private Context context;

    public static MessageRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (MessageRepository.class) {
                instance = new MessageRepository(context);
            }
        }
        return instance;
    }

    private DatabaseHelper mHelper;

    private MessageRepository(Context context) {
        this.context = context;
        if (PreferencesManager.getsInstance(context).getFirstLaunch()) {
            context.deleteDatabase("nfchat.db");
            PreferencesManager.getsInstance(context).setFirstLaunch(false);

        }
        mHelper = OpenHelperManager.getHelper(this.context,
                DatabaseHelper.class);
    }

    public void handleMessageResponse(MessageResponse messageResponse) {
        Log.d(TAG, "handleMessageResponse with API!" +
                messageResponse.getMessage().getMessageType());
        switch (messageResponse.getMessage().getMessageType()) {
            case Constants.MessageType.CAROUSEL:
                setCarousalContent(messageResponse);
                break;
            case Constants.MessageType.INPUT:
                setInputMessage(messageResponse);
                break;
            case Constants.MessageType.SIMPLE:
                setSimpleMessage(messageResponse);
                break;
            case Constants.MessageType.EXTERNAL:
                setExternalMessage(messageResponse);
                break;
            default:
                break;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        OpenHelperManager.releaseHelper();
    }

    public void setCarousalContent(MessageResponse messageResponse) {
        MessageCarousel messageCarousel = new MessageCarousel();
//        if (!messageResponse.isNotifyMessage()) messageCarousel.setEnabled(false);
        try {
            if (!isMessageExist(messageResponse.getMessage().getTimestamp())) {
                if (messageResponse.getData().getContent().getInput() != null)
                    messageCarousel.setInput(messageResponse.getData().getContent().getInput());
                messageCarousel.setMandatory(messageResponse.getData().getContent().getMandatory());
                messageCarousel.setItems(messageResponse.getData().getContent().getItems());
                if (messageResponse.getData().getContent().getItems() != null &&
                        !isMessageExist(messageResponse.getMessage().getTimestamp())) {
                    for (Item item : messageCarousel.getItems()) {
                        item.setOptionsForeignCollection(item.getOptions());
                        for (Option option : item.getOptions()) {
                            item.setMessageCarousel(messageCarousel);
                            option.setItem(item);
                            mHelper.getOptionsDao().create(option);
                        }
                    }
                }
            }
            messageResponse.getMessage().setMessageCarousel(messageCarousel);
            writeMessage(messageResponse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSimpleMessage(MessageResponse messageResponse) {
        try {
            MessageSimple messageSimple = new MessageSimple();
            messageSimple.setMandatory(messageResponse.getData().getContent().getMandatory());
            if (messageResponse.getData().getContent().getMedia() != null)
                messageSimple.setMedia(messageResponse.getData().getContent().getMedia());
            if (messageResponse.getData().getContent().getText() != null)
                messageSimple.setText(messageResponse.getData().getContent().getText());
            messageResponse.getMessage().setMessageSimple(messageSimple);
            writeMessage(messageResponse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setExternalMessage(MessageResponse messageResponse) {
        try {
            messageResponse.getMessage().setExternalMessage(new
                    Gson().toJson(messageResponse.getData().getContent()));
            writeMessage(messageResponse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setInputMessage(MessageResponse messageResponse) {

        try {
            if (!isMessageExist(messageResponse.getMessage().getTimestamp())) {
                MessageInput messageInput = new MessageInput();
                messageInput.setInputType(messageResponse.getData().getContent().getInputType());
                messageInput.setMandatory(messageResponse.getData().getContent().getMandatory());

                switch (messageInput.getInputType()) {
                    case Constants.InputType.LOCATION:
                        InputTypeLocation inputTypeLocation
                                = new InputTypeLocation();
                        inputTypeLocation.setInput(messageResponse.getData().getContent().getInput());
                        inputTypeLocation.setDefaultLocation((messageResponse.getData().
                                getContent().getDefaultLocation()));
                        messageInput.setInputTypeLocation(inputTypeLocation);
                        break;
                    case Constants.InputType.DATE:
                        InputTypeDate inputTypeDate
                                = new InputTypeDate();
                        inputTypeDate.setInput(messageResponse.getData().getContent().getInput());
                        inputTypeDate.setDateRange((messageResponse.getData().
                                getContent().getDateRange()));
                        messageInput.setInputTypeDate(inputTypeDate);
                        break;
                    case Constants.InputType.NUMERIC:
                        InputTypeNumeric inputTypeNumeric
                                = new InputTypeNumeric();
                        inputTypeNumeric.setInput(messageResponse.getData().getContent().getInput());
                        messageInput.setInputTypeNumeric(inputTypeNumeric);
                        break;
                    case Constants.InputType.PHONE:
                        InputTypePhone inputTypePhone
                                = new InputTypePhone();
                        inputTypePhone.setInput(messageResponse.getData().getContent().getInput());
                        messageInput.setInputTypePhone(inputTypePhone);
                        break;
                    case Constants.InputType.ADDRESS:
                        InputTypeAddress inputTypeAddress
                                = new InputTypeAddress();
                        inputTypeAddress.setInput(messageResponse.getData().getContent().getInput());
                        if (messageResponse.getData().getContent().getRequiredFields() != null)
                            inputTypeAddress.setRequiredFields(messageResponse.getData().getContent().
                                    getRequiredFields().toArray(new String
                                    [messageResponse.getData().getContent().getRequiredFields().size()]));
                        messageInput.setInputTypeAddress(inputTypeAddress);
                        break;
                    case Constants.InputType.MEDIA:
                        InputTypeMedia inputTypeMedia
                                = new InputTypeMedia();
                        inputTypeMedia.setInput(messageResponse.getData().getContent().getInput());
                        inputTypeMedia.setMediaType(messageResponse.getData().getContent().getMediaType());
                        messageInput.setInputTypeMedia(inputTypeMedia);
//                        messageResponse.setFileUpload(true);
                        break;
                    case Constants.InputType.TEXT:
                        InputTypeText inputTypeText = new InputTypeText();
                        inputTypeText.setInput(messageResponse.getData().getContent().getInput());
                        inputTypeText.setText(messageResponse.getData().getContent().getText());
                        inputTypeText.setTextInputAttr(messageResponse.getData().
                                getContent().getTextInputAttr());
                        messageInput.setInputTypeText(inputTypeText);
                        break;
                    case Constants.InputType.TIME:
                        InputTypeTime inputTypeTime
                                = new InputTypeTime();
                        inputTypeTime.setInput(messageResponse.getData().getContent().getInput());
                        inputTypeTime.setTimeRange((messageResponse.getData().
                                getContent().getTimeRange()));
                        messageInput.setInputTypeTime(inputTypeTime);
                        break;
                    case Constants.InputType.EMAIL:
                        InputTypeEmail inputTypeEmail
                                = new InputTypeEmail();
                        inputTypeEmail.setInput(messageResponse.getData().getContent().getInput());
                        messageInput.setInputTypeEmail(inputTypeEmail);
                        break;
                    case Constants.InputType.OPTIONS:
                        if (!messageResponse.isOnlyUpdate()) {
                            messageInput.setInputForOptions(messageResponse.
                                    getData().getContent().getInput());
                            messageInput.setOptionsForeignCollection(messageResponse.getData()
                                    .getContent().getOptions());
                            messageResponse.getMessage().setMessageInput(messageInput);
//                            if (!isMessageExist(messageResponse.getMessage().getTimestamp()))
                            for (Option option : messageInput.getOptionsForeignCollection()) {
                                option.setMessageInput(messageInput);
                                mHelper.getOptionsDao().create(option);
                            }
                        }
                        break;
                    case Constants.InputType.LIST:
                        if (!messageResponse.isOnlyUpdate()) {
                            InputTypeList inputTypeList = new InputTypeList();
                            inputTypeList.setInput(messageResponse.
                                    getData().getContent().getInput());
                            inputTypeList.setValuesForeignCollection(messageResponse.getData()
                                    .getContent().getValues());
                            messageInput.setInputTypeList(inputTypeList);
                            messageResponse.getMessage().setMessageInput(messageInput);
                            for (Option option : messageInput.getInputTypeList()
                                    .getValuesForeignCollection()) {
                                option.setInputTypeList(inputTypeList);
                                mHelper.getOptionsDao().create(option);
                            }
                        }
                        break;
                }
                messageResponse.getMessage().setMessageInput(messageInput);
            }
            writeMessage(messageResponse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param timestamp based on message will be searched
     * @return true if exist
     * @throws SQLException
     */
    private Boolean isMessageExist(long timestamp) throws SQLException {
        QueryBuilder<Message, Integer> builder = mHelper.getMessageDao().queryBuilder();
        builder.where().eq("timestamp", timestamp);
        builder.limit(1L);
        List<Message> messages = mHelper.getMessageDao().query(builder.prepare());
        return messages != null && messages.size() > 0;
    }

    private void writeMessage(MessageResponse messageResponse) throws SQLException {
        if (messageResponse.isOnlyUpdate()) {
            updateMessage(messageResponse.getMessage(), messageResponse.getTimestampToUpdate());
            ListenerManager.getInstance().notifyMessageUpdate(messageResponse.getMessage(),
                    messageResponse.getTimestampToUpdate());
            return;
        }
        Message resultMessage = null;
        try {
            resultMessage = mHelper.getMessageDao()
                    .createIfNotExists(messageResponse.getMessage());
            if (!resultMessage.getSyncWithServer()) ApiCalls.sendMessage(context, messageResponse);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (messageResponse.isNotifyMessage())
            ListenerManager.getInstance().notifyNewMessage(resultMessage);
    }

    public List<Message> getMessages() throws SQLException, IOException {
        QueryBuilder<Message, Integer> builder = mHelper.getMessageDao().queryBuilder();
        builder.orderBy("timestamp", false);  // true for ascending, false for descending
        builder.limit(Constants.HISTORY_MESSAGES_LIMIT);
        return mHelper.getMessageDao().query(builder.prepare());  // returns list of 20 items
    }

    public List<Message> loadHistoryMessages(int totalItems) throws SQLException, IOException {
        QueryBuilder<Message, Integer> builder = mHelper.getMessageDao().queryBuilder();
        builder.orderBy("timestamp", false);  // true for ascending, false for descending
        builder.limit(Constants.HISTORY_MESSAGES_LIMIT);
        builder.offset((long) totalItems);
        return mHelper.getMessageDao().query(builder.prepare());
    }

    public void saveHistoryMessages(List<MessageResponse> messageResponses,
                                    Integer messageCount, Integer page) throws IOException, SQLException {
        for (MessageResponse messageResponse : messageResponses) {
            int messageType = messageResponse.getData().getType();
            messageResponse.getMessage().setMessageType(messageType);
            messageResponse.getMessage().setSyncWithServer(true);
            messageResponse.setNotifyMessage(false);
            handleMessageResponse(messageResponse);
        }
        ListenerManager.getInstance().notifyHistoryLoaded(loadHistoryMessages(messageCount), page);
    }

    public void clearTables() {
        mHelper.clearData(mHelper.getWritableDatabase(), mHelper.getConnectionSource());
    }

    public List<Message> getLastMessage() throws SQLException, IOException {
        QueryBuilder<Message, Integer> builder = mHelper.getMessageDao().queryBuilder();
        builder.limit(1L);
        builder.orderBy("timestamp", false);  // true for ascending, false for descending
        return mHelper.getMessageDao().query(builder.prepare());
    }

    public List<Message> getFirstMessage() throws SQLException, IOException {
        QueryBuilder<Message, Integer> builder = mHelper.getMessageDao().queryBuilder();
        builder.limit(1L);
        builder.orderBy("timestamp", true);  // true for ascending, false for descending
        return mHelper.getMessageDao().query(builder.prepare());
    }

    public List<Message> getUnSentMessages() throws SQLException, IOException {
        QueryBuilder<Message, Integer> builder = mHelper.getMessageDao().queryBuilder();
        builder.where().eq("sync_with_server", false);
        builder.orderBy("timestamp", true);
        return mHelper.getMessageDao().query(builder.prepare());
    }

    private int updateMessage(Message message, long time) throws SQLException {
        UpdateBuilder<Message, Integer> updateBuilder
                = mHelper.getMessageDao().updateBuilder();
        updateBuilder.where().eq("timestamp", time);
        updateBuilder.updateColumnValue("sync_with_server", message.getSyncWithServer());
        updateBuilder.updateColumnValue("messageId", message.getMessageId());
        updateBuilder.updateColumnValue("timestamp", message.getTimestamp());
        return updateBuilder.update();
    }

    public int updateMediaMessage(Input input, Integer id) throws SQLException {
        UpdateBuilder<InputTypeMedia, Integer> updateBuilder
                = mHelper.getInputTypeMediaDao().updateBuilder();
        updateBuilder.where().eq("id", id);
        updateBuilder.updateColumnValue("input", input);
        int res = updateBuilder.update();
        //if (res>0)//TODO update views
        return res;
    }

//    public int updateCarouselMessage() throws SQLException {
//        UpdateBuilder<MessageCarousel, Integer> updateBuilder
//                = mHelper.getMessageCarouselDao().updateBuilder();
//        updateBuilder.where().eq("is_enable", true);
//        updateBuilder.updateColumnValue("is_enable",
//                false);
//        return updateBuilder.update();
//    }

}

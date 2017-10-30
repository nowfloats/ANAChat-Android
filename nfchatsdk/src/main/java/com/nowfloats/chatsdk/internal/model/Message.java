package com.nowfloats.chatsdk.internal.model;


import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.nowfloats.chatsdk.internal.model.inputdata.Participant;

import java.util.Date;


@DatabaseTable(tableName = "messages")
public class Message extends BaseModel implements IMessage, MessageContentType {
    @DatabaseField(generatedId = true, index = true)
    public transient int id;

    @DatabaseField(columnName = "messageId")
    @SerializedName("id")
    private String messageId;

    @SerializedName("sender")
    @DatabaseField(columnName = "from", dataType = DataType.SERIALIZABLE)
    private Participant from;

    @SerializedName("recipient")
    @DatabaseField(columnName = "to", dataType = DataType.SERIALIZABLE)
    private Participant to;

    @DatabaseField(columnName = "timestamp", canBeNull = false, index = true, unique = true)
    private long timestamp;

    @DatabaseField(columnName = "senderType")
    private Integer senderType;

    @DatabaseField(columnName = "sessionId")
    private String sessionId;

    @DatabaseField(columnName = "responseTo")
    private String responseTo;

    @SerializedName("type")
    @DatabaseField(columnName = "message_type", canBeNull = false, index = true)
    private transient int messageType;

    @DatabaseField(columnName = "sync_with_server", defaultValue = "false")
    private transient Boolean syncWithServer = false;

    @DatabaseField(columnName = "external_string")
    private String externalMessage;

    @DatabaseField(foreign = true, foreignAutoRefresh = true,
            columnName = "simple_message", foreignAutoCreate = true)
    private transient MessageSimple messageSimple;

    @DatabaseField(foreign = true, foreignAutoRefresh = true,
            columnName = "carousel_message", foreignAutoCreate = true)
    private transient MessageCarousel messageCarousel;

    @DatabaseField(foreign = true, foreignAutoRefresh = true,
            columnName = "input_message", foreignAutoCreate = true, maxForeignAutoRefreshLevel = 6)
    private transient MessageInput messageInput;

    @Override
    public String getText() {
        return messageId;
    }

    @Override
    public String getMId() {
        return String.valueOf(timestamp);
    }

    @Override
    public String getUserId() {
        return from.getId();
    }

    @Override
    public Date getCreatedAt() {
        return new Date(timestamp);
    }

    @Override
    public int getMessageType() {
        return messageType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Participant getFrom() {
        return from;
    }

    public void setFrom(Participant from) {
        this.from = from;
    }

    public Participant getTo() {
        return to;
    }

    public void setTo(Participant to) {
        this.to = to;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getSenderType() {
        return senderType;
    }

    public void setSenderType(Integer senderType) {
        this.senderType = senderType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(String responseTo) {
        this.responseTo = responseTo;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Boolean getSyncWithServer() {
        return syncWithServer;
    }

    public void setSyncWithServer(Boolean syncWithServer) {
        this.syncWithServer = syncWithServer;
    }

    public String getExternalMessage() {
        return externalMessage;
    }

    public void setExternalMessage(String externalMessage) {
        this.externalMessage = externalMessage;
    }

    public MessageSimple getMessageSimple() {
        return messageSimple;
    }

    public void setMessageSimple(MessageSimple messageSimple) {
        this.messageSimple = messageSimple;
    }

    public MessageCarousel getMessageCarousel() {
        return messageCarousel;
    }

    public void setMessageCarousel(MessageCarousel messageCarousel) {
        this.messageCarousel = messageCarousel;
    }

    public MessageInput getMessageInput() {
        return messageInput;
    }

    public void setMessageInput(MessageInput messageInput) {
        this.messageInput = messageInput;
    }
}

package com.nowfloats.chatsdk.internal.model.inputdata;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.nowfloats.chatsdk.internal.model.BaseModel;
import com.nowfloats.chatsdk.internal.model.MessageInput;

/**
 * Created by lookup on 27/09/17.
 */


@DatabaseTable(tableName = "input_media")
public class InputTypeMedia extends BaseModel {

    @DatabaseField(generatedId = true)
    private transient int id;

    @ForeignCollectionField()
    private ForeignCollection<MessageInput> messageInputs;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input input;

    @DatabaseField(columnName = "media_type")
    private int mediaType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ForeignCollection<MessageInput> getMessageInputs() {
        return messageInputs;
    }

    public void setMessageInputs(ForeignCollection<MessageInput> messageInputs) {
        this.messageInputs = messageInputs;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }
}

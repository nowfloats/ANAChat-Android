package com.nowfloats.chatsdk.internal.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.nowfloats.chatsdk.internal.model.inputdata.Input;

@DatabaseTable(tableName = "input_address")
public class InputTypeAddress extends BaseModel {

    @DatabaseField(generatedId = true)
    private transient int id;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input input;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    String[] requiredFields;

    @ForeignCollectionField()
    private ForeignCollection<MessageInput> messageInputs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public String[] getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(String[] requiredFields) {
        this.requiredFields = requiredFields;
    }

    public ForeignCollection<MessageInput> getMessageInputs() {
        return messageInputs;
    }

    public void setMessageInputs(ForeignCollection<MessageInput> messageInputs) {
        this.messageInputs = messageInputs;
    }
}

package com.anachat.chatsdk.internal.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.anachat.chatsdk.internal.model.inputdata.Input;

/**
 * Created by lookup on 28/08/17.
 */


@DatabaseTable(tableName = "input_phone")
public class InputTypePhone extends BaseModel {
    @DatabaseField(generatedId = true)
    int id;

    @ForeignCollectionField()
    private ForeignCollection<MessageInput> messageInputs;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input input;

    @DatabaseField(columnName = "required_fields")
    private String requiredFields;

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

    public String getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(String requiredFields) {
        this.requiredFields = requiredFields;
    }
}

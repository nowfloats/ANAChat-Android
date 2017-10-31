package com.anachat.chatsdk.internal.model;

import com.anachat.chatsdk.internal.model.inputdata.TextInputAttr;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.anachat.chatsdk.internal.model.inputdata.Input;

/**
 * Created by lookup on 25/08/17.
 */


@DatabaseTable(tableName = "input_text")
public class InputTypeText extends BaseModel {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input input;
    @DatabaseField(columnName = "text")
    private String text;
    @DatabaseField(foreign = true, foreignAutoCreate = true,
            foreignAutoRefresh = true, columnName = "text_input_attr")
    TextInputAttr textInputAttr;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextInputAttr getTextInputAttr() {
        return textInputAttr;
    }

    public void setTextInputAttr(TextInputAttr textInputAttr) {
        this.textInputAttr = textInputAttr;
    }

    public ForeignCollection<MessageInput> getMessageInputs() {
        return messageInputs;
    }

    public void setMessageInputs(ForeignCollection<MessageInput> messageInputs) {
        this.messageInputs = messageInputs;
    }
}


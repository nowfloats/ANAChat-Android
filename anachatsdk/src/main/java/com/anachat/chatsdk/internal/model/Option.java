package com.anachat.chatsdk.internal.model;

import com.anachat.chatsdk.internal.model.inputdata.InputTypeList;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "options")
public class Option {
    @DatabaseField(generatedId = true)
    private transient int id;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "value")
    private String value;

    @DatabaseField(columnName = "text")
    private String text;

    private transient Boolean isSelected = false;

    @DatabaseField(columnName = "type")
    Integer type;
    @DatabaseField(foreign = true,
            foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "item", index = true)
    private transient Item item;

    @DatabaseField(foreign = true,
            foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "message_input", index = true)
    private transient MessageInput messageInput;

    @DatabaseField(foreign = true,
            foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "input_type_list", index = true)
    private transient InputTypeList inputTypeList;

    public InputTypeList getInputTypeList() {
        return inputTypeList;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public void setInputTypeList(InputTypeList inputTypeList) {
        this.inputTypeList = inputTypeList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public MessageInput getMessageInput() {
        return messageInput;
    }

    public void setMessageInput(MessageInput messageInput) {
        this.messageInput = messageInput;
    }
}
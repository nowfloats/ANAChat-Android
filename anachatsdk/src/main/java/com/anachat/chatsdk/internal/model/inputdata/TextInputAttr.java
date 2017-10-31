package com.anachat.chatsdk.internal.model.inputdata;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.anachat.chatsdk.internal.model.BaseModel;


@DatabaseTable(tableName = "input_text_data")
public class TextInputAttr extends BaseModel {
    @Expose(serialize = false)
    @DatabaseField(generatedId = true)
    private transient int id;
    @DatabaseField(columnName = "multi_line")
    private int multiLine;
    @DatabaseField(columnName = "min_length")
    private int minLength;
    @DatabaseField(columnName = "max_length")
    private int maxLength;
    @DatabaseField(columnName = "place_holder")
    private String placeHolder;
    @DatabaseField(columnName = "default_text")
    private String defaultText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMultiLine() {
        return multiLine;
    }

    public void setMultiLine(int multiLine) {
        this.multiLine = multiLine;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }
}

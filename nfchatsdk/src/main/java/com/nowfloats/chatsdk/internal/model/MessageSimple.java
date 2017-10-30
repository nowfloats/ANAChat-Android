package com.nowfloats.chatsdk.internal.model;

/**
 * Created by lookup on 17/08/17.
 */


import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "simple_messages")
public class MessageSimple extends BaseModel {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField(columnName = "text")
    private String text;

    @ForeignCollectionField()
    private ForeignCollection<Message> message;

    @DatabaseField(columnName = "mandatory")
    private int mandatory;

    @DatabaseField(columnName = "media", foreign = true,
            foreignAutoRefresh = true, foreignAutoCreate = true)
    private Media media;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ForeignCollection<Message> getMessage() {
        return message;
    }

    public void setMessage(ForeignCollection<Message> message) {
        this.message = message;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}



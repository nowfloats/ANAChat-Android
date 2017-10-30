package com.nowfloats.chatsdk.internal.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by lookup on 24/08/17.
 */


@DatabaseTable(tableName = "media_contents")
public class Media extends BaseModel {
    @DatabaseField(generatedId = true)
    private transient int id;

    @DatabaseField(columnName = "url")
    private String url;

    @DatabaseField(columnName = "type")
    private int type;

    @DatabaseField(columnName = "preview_url")
    private String previewUrl;

    @ForeignCollectionField()
    private transient ForeignCollection<Item> items;

    @ForeignCollectionField()
    private transient ForeignCollection<MessageSimple> messagesSimple;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public ForeignCollection<Item> getItems() {
        return items;
    }

    public void setItems(ForeignCollection<Item> items) {
        this.items = items;
    }

    public ForeignCollection<MessageSimple> getMessagesSimple() {
        return messagesSimple;
    }

    public void setMessagesSimple(ForeignCollection<MessageSimple> messagesSimple) {
        this.messagesSimple = messagesSimple;
    }
}
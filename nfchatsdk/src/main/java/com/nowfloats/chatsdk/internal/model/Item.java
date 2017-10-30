package com.nowfloats.chatsdk.internal.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.List;

@DatabaseTable(tableName = "carousel_item")
public class Item {
    @DatabaseField(generatedId = true)
    private transient int id;
    @DatabaseField(columnName = "tittle")
    private String title;
    @DatabaseField(columnName = "url")
    private String url;
    @DatabaseField(columnName = "desc")
    private String desc;
    @DatabaseField(foreign = true, columnName = "media",
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private Media media;
    //    @DatabaseField(dataType = DataType.SERIALIZABLE)

    List<Option> options;
    @SerializedName("optionsForeign")
    @ForeignCollectionField(eager = true)
    private transient Collection<Option> optionsForeignCollection;

    @DatabaseField(foreign = true,
            foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "messageCarousel")
    private transient MessageCarousel messageCarousel;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Collection<Option> getOptionsForeignCollection() {
        return optionsForeignCollection;
    }

    public void setOptionsForeignCollection(Collection<Option> optionsForeignCollection) {
        this.optionsForeignCollection = optionsForeignCollection;
    }

    public MessageCarousel getMessageCarousel() {
        return messageCarousel;
    }

    public void setMessageCarousel(MessageCarousel messageCarousel) {
        this.messageCarousel = messageCarousel;
    }
}

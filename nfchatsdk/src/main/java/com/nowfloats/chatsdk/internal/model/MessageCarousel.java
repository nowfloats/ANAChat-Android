package com.nowfloats.chatsdk.internal.model;

/**
 * Created by lookup on 17/08/17.
 */

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.nowfloats.chatsdk.internal.model.inputdata.Input;

import java.util.Collection;



@DatabaseTable(tableName = "carousel_messages")
public class MessageCarousel extends BaseModel {
    @DatabaseField(generatedId = true)
    private int id;

    @ForeignCollectionField()
    protected transient ForeignCollection<Message> message;

    @ForeignCollectionField(eager = true)
    private transient Collection<Item> items;

    @DatabaseField(columnName = "mandatory")
    int mandatory;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input input;

    @DatabaseField(columnName = "is_enable", defaultValue = "true")
    Boolean isEnabled = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ForeignCollection<Message> getMessage() {
        return message;
    }

    public void setMessage(ForeignCollection<Message> message) {
        this.message = message;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}

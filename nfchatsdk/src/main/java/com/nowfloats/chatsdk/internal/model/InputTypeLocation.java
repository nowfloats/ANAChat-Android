package com.nowfloats.chatsdk.internal.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.nowfloats.chatsdk.internal.model.inputdata.DefaultLocation;
import com.nowfloats.chatsdk.internal.model.inputdata.Input;

/**
 * Created by lookup on 28/08/17.
 */

@DatabaseTable(tableName = "input_location")
public class InputTypeLocation extends BaseModel {

    @DatabaseField(generatedId = true)
    int id;
//    @DatabaseField(columnName = "input_json")
//    private String inputJson;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input input;
    @DatabaseField(columnName = "default_location", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private DefaultLocation defaultLocation;


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

    public DefaultLocation getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(DefaultLocation defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public ForeignCollection<MessageInput> getMessageInputs() {
        return messageInputs;
    }

    public void setMessageInputs(ForeignCollection<MessageInput> messageInputs) {
        this.messageInputs = messageInputs;
    }
}

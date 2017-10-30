package com.nowfloats.chatsdk.internal.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.nowfloats.chatsdk.internal.model.inputdata.DateRange;
import com.nowfloats.chatsdk.internal.model.inputdata.Input;

/**
 * Created by lookup on 28/08/17.
 */

@DatabaseTable(tableName = "input_date")
public class InputTypeDate extends BaseModel {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input input;

    @DatabaseField(columnName = "date_range", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private DateRange dateRange;

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

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public ForeignCollection<MessageInput> getMessageInputs() {
        return messageInputs;
    }

    public void setMessageInputs(ForeignCollection<MessageInput> messageInputs) {
        this.messageInputs = messageInputs;
    }
}

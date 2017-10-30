package com.nowfloats.chatsdk.internal.model.inputdata;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.nowfloats.chatsdk.internal.model.BaseModel;

/**
 * Created by lookup on 28/08/17.
 */


@DatabaseTable(tableName = "input_date_data")
public class DateRange extends BaseModel {

    @DatabaseField(generatedId = true)
    private transient int id;
    @DatabaseField(columnName = "min", dataType = DataType.SERIALIZABLE)
    private Date min;
    @DatabaseField(columnName = "max", dataType = DataType.SERIALIZABLE)
    private Date max;
    @DatabaseField(columnName = "interval")
    private String interval;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getMin() {
        return min;
    }

    public void setMin(Date min) {
        this.min = min;
    }

    public Date getMax() {
        return max;
    }

    public void setMax(Date max) {
        this.max = max;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}

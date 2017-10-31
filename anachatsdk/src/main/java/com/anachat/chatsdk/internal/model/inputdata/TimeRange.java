package com.anachat.chatsdk.internal.model.inputdata;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.anachat.chatsdk.internal.model.BaseModel;

/**
 * Created by lookup on 28/08/17.
 */

@DatabaseTable(tableName = "input_time_data")
public class TimeRange extends BaseModel {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField(columnName = "min", dataType = DataType.SERIALIZABLE)
    private Time min;
    @DatabaseField(columnName = "max", dataType = DataType.SERIALIZABLE)
    private Time max;
    @DatabaseField(columnName = "interval")
    private String interval;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getMin() {
        return min;
    }

    public void setMin(Time min) {
        this.min = min;
    }

    public Time getMax() {
        return max;
    }

    public void setMax(Time max) {
        this.max = max;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}

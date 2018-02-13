package com.anachat.chatsdk.internal.model.inputdata;

import com.anachat.chatsdk.internal.model.Option;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lookup on 13/11/17.
 */
@DatabaseTable(tableName = "input_list")
public class InputTypeList {


    @DatabaseField(generatedId = true)
    private transient int id;

    @DatabaseField()
    private Boolean multiple;

    private List<Option> values;
    @SerializedName("optionsForeign")
    @ForeignCollectionField(eager = true)
    private transient Collection<Option> valuesForeignCollection;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input input;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Option> getValues() {
        return values;
    }

    public void setValues(List<Option> values) {
        this.values = values;
    }

    public Collection<Option> getValuesForeignCollection() {
        return valuesForeignCollection;
    }

    public void setValuesForeignCollection(Collection<Option> valuesForeignCollection) {
        this.valuesForeignCollection = valuesForeignCollection;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public List<Option> getValuesAsList() {
        return new ArrayList<>(valuesForeignCollection);
    }
}

package com.anachat.chatsdk.internal.model;

import com.anachat.chatsdk.internal.model.inputdata.InputTypeMedia;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.anachat.chatsdk.internal.model.inputdata.Input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * Created by lookup on 23/08/17.
 */


@DatabaseTable(tableName = "input_messages")
public class MessageInput extends BaseModel {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField(columnName = "input_type")
    int inputType;
    @DatabaseField(columnName = "mandatory")
    private int mandatory;

    @DatabaseField(columnName = "requiredFields")
    private String requiredFields;

    @DatabaseField(columnName = "input_type_text", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypeText inputTypeText;

    @DatabaseField(columnName = "input_type_location", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypeLocation inputTypeLocation;

    @DatabaseField(columnName = "input_type_date", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypeDate inputTypeDate;

    @DatabaseField(columnName = "input_type_email", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypeEmail inputTypeEmail;

    @DatabaseField(columnName = "input_type_numeric", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypeNumeric inputTypeNumeric;
    @DatabaseField(columnName = "input_type_phone", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypePhone inputTypePhone;

    @DatabaseField(columnName = "input_type_time", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypeTime inputTypeTime;

    @DatabaseField(columnName = "input_type_address", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypeAddress inputTypeAddress;

    @DatabaseField(columnName = "input_type_media", foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    private InputTypeMedia inputTypeMedia;

//    @ForeignCollectionField()
//    private ForeignCollection<Message> message;

    @ForeignCollectionField(eager = true)
    private transient Collection<Option> optionsForeignCollection;


    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Input inputForOptions;

    public List<Option> getOptionsAsList() {
        return new ArrayList<>(optionsForeignCollection);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public String getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(String requiredFields) {
        this.requiredFields = requiredFields;
    }

    public InputTypeText getInputTypeText() {
        return inputTypeText;
    }

    public void setInputTypeText(InputTypeText inputTypeText) {
        this.inputTypeText = inputTypeText;
    }

    public InputTypeLocation getInputTypeLocation() {
        return inputTypeLocation;
    }

    public void setInputTypeLocation(InputTypeLocation inputTypeLocation) {
        this.inputTypeLocation = inputTypeLocation;
    }

    public InputTypeDate getInputTypeDate() {
        return inputTypeDate;
    }

    public void setInputTypeDate(InputTypeDate inputTypeDate) {
        this.inputTypeDate = inputTypeDate;
    }

    public InputTypeEmail getInputTypeEmail() {
        return inputTypeEmail;
    }

    public void setInputTypeEmail(InputTypeEmail inputTypeEmail) {
        this.inputTypeEmail = inputTypeEmail;
    }

    public InputTypeNumeric getInputTypeNumeric() {
        return inputTypeNumeric;
    }

    public void setInputTypeNumeric(InputTypeNumeric inputTypeNumeric) {
        this.inputTypeNumeric = inputTypeNumeric;
    }

    public InputTypePhone getInputTypePhone() {
        return inputTypePhone;
    }

    public void setInputTypePhone(InputTypePhone inputTypePhone) {
        this.inputTypePhone = inputTypePhone;
    }

    public InputTypeTime getInputTypeTime() {
        return inputTypeTime;
    }

    public void setInputTypeTime(InputTypeTime inputTypeTime) {
        this.inputTypeTime = inputTypeTime;
    }

    public InputTypeAddress getInputTypeAddress() {
        return inputTypeAddress;
    }

    public void setInputTypeAddress(InputTypeAddress inputTypeAddress) {
        this.inputTypeAddress = inputTypeAddress;
    }

    public InputTypeMedia getInputTypeMedia() {
        return inputTypeMedia;
    }

    public void setInputTypeMedia(InputTypeMedia inputTypeMedia) {
        this.inputTypeMedia = inputTypeMedia;
    }

    public Collection<Option> getOptionsForeignCollection() {
        return optionsForeignCollection;
    }

    public void setOptionsForeignCollection(Collection<Option> optionsForeignCollection) {
        this.optionsForeignCollection = optionsForeignCollection;
    }

    public Input getInputForOptions() {
        return inputForOptions;
    }

    public void setInputForOptions(Input inputForOptions) {
        this.inputForOptions = inputForOptions;
    }
}

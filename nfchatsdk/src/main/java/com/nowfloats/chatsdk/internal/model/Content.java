package com.nowfloats.chatsdk.internal.model;

import com.nowfloats.chatsdk.internal.model.inputdata.DateRange;
import com.nowfloats.chatsdk.internal.model.inputdata.DefaultLocation;
import com.nowfloats.chatsdk.internal.model.inputdata.Input;
import com.nowfloats.chatsdk.internal.model.inputdata.TextInputAttr;
import com.nowfloats.chatsdk.internal.model.inputdata.TimeRange;

import java.util.List;


public class Content extends BaseModel {
    private String text;
    private int mandatory;
    private List<Item> items;
    private Media media;


    //For input messages
    private Integer mediaType;
    private Integer inputType;
    private DefaultLocation defaultLocation;
    private Input input;
    private DateRange dateRange;
    private List<String> requiredFields;
    private TextInputAttr textInputAttr;
    private TimeRange timeRange;
    private List<Option> options;
    private List<Option> values;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getInputType() {
        return inputType;
    }

    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    public DefaultLocation getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(DefaultLocation defaultLocation) {
        this.defaultLocation = defaultLocation;
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

    public List<String> getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(List<String> requiredFields) {
        this.requiredFields = requiredFields;
    }

    public TextInputAttr getTextInputAttr() {
        return textInputAttr;
    }

    public void setTextInputAttr(TextInputAttr textInputAttr) {
        this.textInputAttr = textInputAttr;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public List<Option> getValues() {
        return values;
    }

    public void setValues(List<Option> values) {
        this.values = values;
    }
}
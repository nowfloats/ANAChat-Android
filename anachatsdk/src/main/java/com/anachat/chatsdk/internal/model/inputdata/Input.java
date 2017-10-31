package com.anachat.chatsdk.internal.model.inputdata;

import com.anachat.chatsdk.internal.model.BaseModel;
import com.anachat.chatsdk.internal.model.Media;

import java.util.List;

/**
 * Created by lookup on 29/08/17.
 */


public class Input extends BaseModel {
    private String val;
    private String input;
    private DefaultLocation location;
    private Date date;
    private Time time;
    private Address address;
    private List<Media> media;
    private String optionText;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public DefaultLocation getLocation() {
        return location;
    }

    public void setLocation(DefaultLocation location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}

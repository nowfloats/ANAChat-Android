package com.anachat.chatsdk.internal.model;

/**
 * Created by lookup on 04/01/18.
 */

public class Event extends BaseModel {

    private Integer type;
    private String data;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

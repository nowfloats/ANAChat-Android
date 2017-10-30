package com.nowfloats.chatsdk.internal.model;

/**
 * Created by lookup on 28/08/17.
 */


public class Data extends BaseModel {
    private int type;
    private Content content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
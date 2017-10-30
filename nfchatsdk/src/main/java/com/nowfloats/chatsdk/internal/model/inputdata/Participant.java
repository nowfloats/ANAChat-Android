package com.nowfloats.chatsdk.internal.model.inputdata;

import com.nowfloats.chatsdk.internal.model.BaseModel;

/**
 * Created by lookup on 19/09/17.
 */


public class Participant extends BaseModel {
    String id;
    int medium;

    public Participant(String id, int medium) {
        this.id = id;
        this.medium = medium;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }
}

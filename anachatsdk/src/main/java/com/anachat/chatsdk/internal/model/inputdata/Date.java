package com.anachat.chatsdk.internal.model.inputdata;

import com.anachat.chatsdk.internal.model.BaseModel;

/**
 * Created by lookup on 29/08/17.
 */

public class Date extends BaseModel {
    String year;
    String month;
    String mday;

    public Date(String year, String month, String mday) {
        this.year = year;
        this.month = month;
        this.mday = mday;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMday() {
        return mday;
    }

    public void setMday(String mday) {
        this.mday = mday;
    }
}

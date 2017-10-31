package com.anachat.chatsdk.internal.model.inputdata;

import com.anachat.chatsdk.internal.model.BaseModel;

/**
 * Created by lookup on 29/08/17.
 */

public class Time extends BaseModel {
    String hour;
    String minute;
    String second;

    public Time(String hour, String minute, String second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}

package com.nowfloats.chatsdk.internal.model.inputdata;

import com.nowfloats.chatsdk.internal.model.BaseModel;

/**
 * Created by lookup on 29/08/17.
 */

public class Address extends BaseModel {
    String line1;
    String area;
    String city;
    String state;
    String country;
    String pin;

    public Address(String line1, String area, String city, String state, String country, String pin) {
        this.line1 = line1;
        this.area = area;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pin = pin;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}

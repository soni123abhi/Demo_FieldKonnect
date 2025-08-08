package com.exp.clonefieldkonnect.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkTypeModel {


    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("is_city")
    @Expose
    private Boolean isCity;

    @SerializedName("is_beat")
    @Expose
    private Boolean is_beat;

    public Boolean getIs_beat() {
        return is_beat;
    }

    public void setIs_beat(Boolean is_beat) {
        this.is_beat = is_beat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getCity() {
        return isCity;
    }

    public void setCity(Boolean city) {
        isCity = city;
    }
}

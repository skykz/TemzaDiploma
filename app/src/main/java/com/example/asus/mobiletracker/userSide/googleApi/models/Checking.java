package com.example.asus.mobiletracker.userSide.googleApi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Checking {

    @SerializedName("curier_id")
    @Expose
    private String curier_id;


    public String getCurier_id() {
        return curier_id;
    }

    public void setCurier_id(String curier_id) {
        this.curier_id = curier_id;
    }
}

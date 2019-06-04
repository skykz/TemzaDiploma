package com.example.asus.mobiletracker.userSide.googleApi.models;

public class Tracking {

    private String email,uid,lng,lat;

    public Tracking(){}

    public Tracking(String email, String uid, String lng, String lat) {
        this.email = email;
        this.uid = uid;
        this.lng = lng;
        this.lat = lat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}

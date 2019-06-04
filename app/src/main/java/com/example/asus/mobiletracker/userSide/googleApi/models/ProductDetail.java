package com.example.asus.mobiletracker.userSide.googleApi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//TODO: finish single product class
public class ProductDetail {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("material")
    @Expose
    private String material;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("curier_id")
    @Expose
    private String curier_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurier_id() {
        return curier_id;
    }

    public void setCurier_id(String curier_id) {
        this.curier_id = curier_id;
    }

    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }


    @SerializedName("serial_number")
    @Expose
    private int serial_number;


}

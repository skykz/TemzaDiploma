package com.example.asus.mobiletracker.userSide.googleApi.models;


import java.util.Date;

public class Products {

    private int id;
    private String title;
    private double price;
    private String status;
    private String image;
    private String material;
    private String country;
    private String curier_id;
    private int serial_number;
    private String body;
//    private Date created_at;

    public String getCurier_id() {
        return curier_id;
    }

    public void setCurier_id(String curier_id) {
        this.curier_id = curier_id;
    }

    public Products(int id, String title, String curier_id, double price, String image, String status, String material, String country, String body) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.curier_id = curier_id;
        this.image = image;
        this.status = status;
        this.body = body;
        this.country = country;
        this.material = material;
//        this.created_at = created_at;
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

    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getName_of_product() {
        return title;
    }

    public void setName_of_product(String name_of_product) {
        this.title = name_of_product;
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
}

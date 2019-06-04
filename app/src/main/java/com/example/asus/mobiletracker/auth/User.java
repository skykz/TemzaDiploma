package com.example.asus.mobiletracker.auth;

public class User {
    private String email, firstname, lastname, number, status, profession;
    public User(){

    }

    public User(String email, String firstname, String lastname, String number, String status, String profession) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.number = number;
        this.status = status;
        this.profession = profession;
    }

    public User(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}

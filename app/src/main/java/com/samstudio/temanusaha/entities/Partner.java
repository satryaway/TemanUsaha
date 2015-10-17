package com.samstudio.temanusaha.entities;

/**
 * Created by satryaway on 10/7/2015.
 * model for partner
 */
public class Partner {
    private int id, status;
    private String name, company, date, imgURL, phoneNumber;
    private double lat, lng;

    public Partner(int id, String name, String company, double lat, double lng, int status) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.date = "27 Agustus 2015 10:00";
        this.phoneNumber = "089696296962";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

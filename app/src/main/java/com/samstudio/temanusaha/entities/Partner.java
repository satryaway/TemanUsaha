package com.samstudio.temanusaha.entities;

/**
 * Created by satryaway on 10/7/2015.
 * model for partner
 */
public class Partner {
    private int id;
    private String name, company;
    private double lat, lng;

    public Partner(int id, String name, String company, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.lat = lat;
        this.lng = lng;
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
}

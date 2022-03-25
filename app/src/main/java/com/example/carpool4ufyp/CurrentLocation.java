package com.example.carpool4ufyp;

public class CurrentLocation {
    double lat;
    double lng;
    String timestamp;
    String userID;

    public CurrentLocation(double lat, double lng, String timestamp, String userID) {
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
        this.userID = userID;
    }

    public CurrentLocation() {

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}


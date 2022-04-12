package com.example.carpool4ufyp;

public class Booking {

    public String driver, passenger, meetingPoint, destination, date, pickupTime, price, driverID, passengerID;
    public Booking(){

    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public Booking(String driver, String passenger, String meetingPoint, String destination, String date, String pickupTime, String price, String driverID, String passengerID){
        this.driver = driver;
        this.passenger = passenger;
        this.meetingPoint = meetingPoint;
        this.destination = destination;
        this.date = date;
        this.pickupTime = pickupTime;
        this.price = price;
        this.driverID = driverID;
        this.passengerID = passengerID;
    }
}

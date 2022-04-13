package com.example.carpool4ufyp;

public class Booking {

    public String driver, passenger, meetingPoint, destination, date, pickupTime, price, driverID, passengerID, bookingID, status;
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

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Booking(String driver, String passenger, String meetingPoint, String destination, String date, String pickupTime, String price, String driverID, String passengerID, String bookingID, String status){
        this.driver = driver;
        this.passenger = passenger;
        this.meetingPoint = meetingPoint;
        this.destination = destination;
        this.date = date;
        this.pickupTime = pickupTime;
        this.price = price;
        this.driverID = driverID;
        this.passengerID = passengerID;
        this.bookingID = bookingID;
        this.status = status;
    }
}

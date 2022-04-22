package com.example.carpool4ufyp;

public class Booking {

    public String driverName, passengerName, meetingPoint, destination, date, pickupTime, driverID, passengerID, bookingID, status;
    public double price;
    public Booking(){

    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriver(String driverName) {
        this.driverName = driverName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public Booking(String driverName, String passengerName, String meetingPoint, String destination, String date, String pickupTime, double price, String driverID, String passengerID, String bookingID, String status){
        this.driverName = driverName;
        this.passengerName = passengerName;
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

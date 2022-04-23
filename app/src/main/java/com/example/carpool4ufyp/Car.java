package com.example.carpool4ufyp;

public class Car {

    public String registrationNumber, licenceExpiration, licenceNumber, numberOfSeats, makeAndModel, colour, userID, carID;
    public Car(){

    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(String numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getLicenceExpiration() {
        return licenceExpiration;
    }

    public void setLicenceExpiration(String licenceExpiration) {
        this.licenceExpiration = licenceExpiration;
    }

    public String getMakeAndModel() {
        return makeAndModel;
    }

    public void setMakeAndModel(String makeAndModel) {
        this.makeAndModel = makeAndModel;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Car(String licenceNumber, String registrationNumber, String numberOfSeats, String licenceExpiration, String makeAndModel,String colour, String userID, String carID){
        this.licenceNumber = licenceNumber;
        this.registrationNumber = registrationNumber;
        this.numberOfSeats = numberOfSeats;
        this.licenceExpiration = licenceExpiration;
        this.makeAndModel = makeAndModel;
        this.colour = colour;
        this.userID = userID;
        this.carID = carID;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }
}

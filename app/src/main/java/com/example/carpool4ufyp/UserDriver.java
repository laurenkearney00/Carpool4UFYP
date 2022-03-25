package com.example.carpool4ufyp;

public class UserDriver {

    public String fullName, dateOfBirth, email, phoneNumber, driverID;

    public UserDriver(){

    }

    public UserDriver(String fullName, String dateOfBirth, String email, String phoneNumber, String driverID){
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.driverID = driverID;
    }

    public String getFullName() {
        return fullName;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() { return email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() { return phoneNumber;
    }
    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverID() { return driverID;
    }
}



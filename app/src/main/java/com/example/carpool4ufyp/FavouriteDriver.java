package com.example.carpool4ufyp;

public class FavouriteDriver {

    public String fullName, driverID;

    public FavouriteDriver(){

    }

    public FavouriteDriver(String fullName, String driverID){
        this.fullName = fullName;
        this.driverID = driverID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverID() { return driverID;
    }
}



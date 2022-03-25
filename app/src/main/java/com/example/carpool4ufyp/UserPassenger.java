package com.example.carpool4ufyp;

public class UserPassenger {

    public String fullName, dateOfBirth, phoneNumber, email, passengerID;


        public String getFullName() {
            return fullName;
        }

        public String getPassengerID() {
        return passengerID;
    }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setFullName(String fullName) {
            this.fullName= fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }


    public UserPassenger(){

    }

    public UserPassenger(String fullName, String dateOfBirth, String email, String phoneNumber, String passengerID){
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passengerID = passengerID;
    }
}


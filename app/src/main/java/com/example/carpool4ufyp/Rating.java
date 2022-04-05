package com.example.carpool4ufyp;

public class Rating {
    private String rating;
    private String sender;
    private String receiver;
    private String ratingID;

    public Rating(){

    }

    public String getRating() {
        return rating;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }


    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Rating(String rating, String receiver, String sender, String ratingID) {
        this.rating = rating;
        this.receiver = receiver;
        this.sender = sender;
        this.ratingID = ratingID;
    }


}

package com.example.carpool4ufyp;

public class Rating {
    private String rating;
    private String senderID;
    private String receiverID;
    private String ratingID;

    public Rating(){

    }

    public String getRating() {
        return rating;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }


    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Rating(String rating, String receiverID, String senderID, String ratingID) {
        this.rating = rating;
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.ratingID = ratingID;
    }


}

package com.example.carpool4ufyp;

public class Notification {
    private String message;
    private String senderID;
    private String receiverID;
    private String timestamp;

    public Notification(){

    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Notification(String message, String receiverID, String senderID, String timestamp) {
        this.message = message;
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.timestamp = timestamp;
    }


}

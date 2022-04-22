package com.example.carpool4ufyp;

public class Message {
    private String message;
    private String senderID;
    private String receiverID;
    private String timestamp;
    private String messageID;

    public Message(){

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

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
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

    public Message(String message, String receiverID, String senderID, String timestamp, String messageID) {
        this.message = message;
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.timestamp = timestamp;
        this.messageID = messageID;
    }


}

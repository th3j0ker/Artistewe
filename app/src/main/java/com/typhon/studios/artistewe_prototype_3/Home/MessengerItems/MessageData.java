package com.typhon.studios.artistewe_prototype_3.Home.MessengerItems;

public class MessageData {
    String message;
    String uniqueid;



    public MessageData(String message, String uniqueid) {

        this.message = message;
        this.uniqueid = uniqueid;
    }

    public MessageData() {
    }

    public MessageData(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }
}

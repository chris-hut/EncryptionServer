package com.hut;

/**
 * Request class modelling a request to be sent from a client to its server
 */
public class Request {

    private String userName;
    private String message;
    private TYPE type;

    public enum TYPE{
      AUTHENTICATE, REQUEST_FILE, FINISH
    };

    public Request(String userName, String message, TYPE type){
        this.userName = userName;
        this.message = message;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public TYPE getType() {
        return type;
    }
}

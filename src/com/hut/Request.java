package com.hut;

import java.io.Serializable;

/**
 * Request class modelling a request to be sent from a client to its server
 */
public class Request implements Serializable{

    private final static long serialVersionUID = 108l;

    public final static String AUTHENTICATE = "authenticate me please";
    public final static String FINISH = "please go away";

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

    public boolean isRequest(){
        return type == TYPE.REQUEST_FILE;
    }

    public boolean isAuthenticate(){
        return type == TYPE.AUTHENTICATE;
    }

    public boolean isFinish(){
        return type == TYPE.FINISH;
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

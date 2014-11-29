package com.hut;

import java.io.Serializable;

/**
 * Request class modelling a request to be sent from a client to its server
 */
public class Request implements Serializable{

    private final static long serialVersionUID = 108l;

    public final static String AUTHENTICATE = "authenticate me please";
    public final static String FINISH = "please go away";

    private String message;
    private TYPE type;

    public enum TYPE{
      AUTHENTICATE, REQUEST_FILE, FINISH
    };

    public Request(String message, TYPE type){
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

    public String getMessage() {
        return message;
    }

    public TYPE getType() {
        return type;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(" message: ").append(this.message)
                .append(" type: ").append(this.type.toString());
        return sb.toString();
    }
}

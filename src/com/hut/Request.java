package com.hut;

import java.io.Serializable;

/**
 * Request class modelling a request to be sent from a client to its server
 */
public class Request implements Serializable{

    private final static long serialVersionUID = 108l;

    public final static String FINISH = "please go away";

    private final byte[] message;
    private final TYPE type;

    public enum TYPE{
      AUTHENTICATE, REQUEST_FILE, FINISH
    }

    public Request(byte[] message, TYPE type){
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

    public String getMessageString() {return new String(message);}

    public byte[] getMessage() {
        return message;
    }

    public TYPE getType() {
        return type;
    }

    public String toString(){
        return "Message: " + new String(this.message) + " type: " + this.type.toString();
    }
}

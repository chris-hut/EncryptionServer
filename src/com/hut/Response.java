package com.hut;

import java.io.Serializable;

/**
 * A Response from a server
 */
public class Response implements Serializable{

    private final static long serialVersionUID = 4815162342L;

    public final static String UNAUTHORIZED_MESSAGE = "you can't see that";
    public final static String FILE_NOT_FOUND_MESSAGE = "can't help you here";
    public final static String SERVER_ERROR_MESSAGE = "something broke on our end";
    public final static String AUTHORIZED_MESSAGE = "you got some good credentials";
    public final static String FILE_TOO_LARGE_MESSAGE = "file was too large";

    public final static int OK = 200;
    public final static int UNAUTHORIZED = 401;
    public final static int FILE_NOT_FOUND = 404;
    public final static int FILE_TOO_LARGE = 413;
    public final static int SERVER_ERROR = 500;

    private final int statusCode;
    private final byte[] message;


    public Response(int statusCode, byte[] message){
        this.statusCode = statusCode;
        this.message = message;
    }

    // TODO: Make error responses when exception occurred when sending request

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getMessage(){
        return message;
    }

    public String getMessageString() {
        return new String(message);
    }

    public String toString(){
        return "Statuscode: " + this.statusCode + " message: " + new String(message);
    }

}

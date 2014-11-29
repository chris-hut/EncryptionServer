package com.hut;

import java.io.Serializable;

/**
 * A Response from a server
 */
public class Response implements Serializable{

    private final static long serialVersionUID = 4815162342L;

    public final static String UNAUTHORIZED_MESSAGE = "you can't see that";
    public final static String NOT_FOUND_MESSAGE = "can't help you here";
    public final static String SERVER_ERROR_MESSAGE = "something broke on our end";
    public final static String AUTHORIZED_MESSAGE = "you got some good credentials";

    public final static int OK = 200;
    public final static int UNAUTHORIZED = 401;
    public final static int NOT_FOUND = 404;
    public final static int SERVER_ERROR = 500;

    private int statusCode;
    private String message;


    public Response(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    // TODO: Make error responses when exception occurred when sending request

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Statuscode: ").append(this.statusCode).append(" message: ").append(message);
        return sb.toString();
    }

}

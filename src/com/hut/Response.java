package com.hut;

/**
 * A Response from a server
 */
public class Response {

    public final static String UNAUTHORIZED_STRING = "you can't see that";
    public final static String NOT_FOUND_MESSAGE = "can't help you here";
    public final static String SERVER_ERROR_MESSAGE = "something broke on our end";

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

}

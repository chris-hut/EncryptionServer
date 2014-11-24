package com.hut;

/**
 * A Response from a server
 */
public class Response {

    private int statusCode;
    private byte message;
    private String header;

    public Response(int statusCode, byte message, String header){
        this.statusCode = statusCode;
        this.message = message;
        this.header = header;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte getMessage() {
        return message;
    }

    public String getHeader() {
        return header;
    }
}

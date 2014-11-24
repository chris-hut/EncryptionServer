package com.hut.client;

import com.hut.Request;
import com.hut.Response;

/**
 * Client class
 */
public class Client {

    private String userName;
    private String key;
    private String serverAddress;

    public Client(String userName, String key, String serverAddress){
        this.userName = userName;
        this.key = key;
        this.serverAddress = serverAddress;
    }

    /**
     * Connects to server with serverAddress
     */
    public void connectToServer(){

    }

    /**
     * Sends a request to the server
     * @param r request in question
     */
    public void sendRequest(Request r){

    }

    /**
     * Decrypts response from server
     */
    public void decryptResponse(Response r){

    }

    /**
     * Closes connection with server
     */
    public void finishConnection(){

    }

}

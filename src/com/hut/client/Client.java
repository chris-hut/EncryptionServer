package com.hut.client;

import com.hut.Request;
import com.hut.Response;

/**
 * Client class
 */
public class Client {

    private String userName;
    private String key;
    private String hostName;

    public Client(String hostName, String userName, String key){
        this.userName = userName;
        this.key = key;
        this.hostName = hostName;
    }

    /**
     * Connects to server with hostName
     */
    public void connectToServer(int portNumber){

    }

    /**
     * Requests fileName from the server and returns the response from the server
     * @param fileName requested from server
     * @return Response from server
     */
    public Response requestFile(String fileName){

        return null;
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


    public String getUserName(){
        return userName;
    }
}

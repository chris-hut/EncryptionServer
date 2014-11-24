package com.hut.server;

import com.hut.Request;

/**
 *
 */
public class ServerThread {

    private String userKey;

    public ServerThread(String userKey){
        this.userKey = userKey;
    }

    /**
     * Parses a clients request
     */
    public void parseRequest(Request r){

    }

    /**
     * Tries to send fileName
     * @param fileName the file in question
     */
    public void sendFile(String fileName){

    }

    /**
     * Sends an acknowledge response to the client
     */
    public void acknowledge(){

    }

    /**
     * Cleans up nicely
     */
    public void finish(){

    }

}

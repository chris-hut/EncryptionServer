package com.hut.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Server class that handles connections and creates new ServerThreads
 */
public class Server {

    List<ServerThread> threads;

    public Server(){
        this.threads = new ArrayList<ServerThread>();
    }

    /**
     * Main loop of server class, handles incoming connections and creates the new
     * ServerThreads.
     *
     * Authenticates users
     */
    public void loopityDoopity(){

    }

}

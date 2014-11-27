package com.hut.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Server class that handles connections and creates new ServerThreads
 */
public class Server {

    private HashMap<String, String> users = new HashMap<String, String>();
    private int portNumber;
    private boolean alive = true;
    private List<ServerThread> threads;

    public Server(int portNumber, HashMap<String, String> users){
        this.users = users;
        this.portNumber = portNumber;
        threads = new ArrayList<ServerThread>();
    }

    /**
     * Main loop of server class, handles incoming connections and creates the new
     * ServerThreads.
     * <p>
     *     > mwf serving
     * </p>
     *
     * Authenticates users
     */
    public void serve(){
        ServerSocket socket = null;
        ServerThread thread = null;
        try{
            socket = new ServerSocket(this.portNumber);

            /*
                Loop forever, whenever someone attaches to us, we create a new ServerThread giving it
                the resulting Socket, and throw the ServerThread in our list of threads.
             */
            while(alive){
                thread = new ServerThread(socket.accept(), users);
                // TODO: Will this add all separate server threads in the list?
                threads.add(thread);
                thread.start();
            }

            this.close();
            socket.close();
        }catch(IOException e){
            if(socket != null && !socket.isClosed()){
                try{
                    socket.close();
                }catch(IOException e1){
                    // Ignore
                }
            }
            System.err.println("Fatal Server Error.");
        }

    }

    /**
     * Closes the server*/
    public void close(){
        /*
        TODO: Find a better way to close the server, since ServerSocket.accept() will block
        until a connection is made, this /might/ make it throw a IOException causing us to leave the loop
        which gets the job done, but is not good control flow at all.
         */
        this.alive = false;
        for(ServerThread thread : threads){
            thread.finish();
        }
    }

}

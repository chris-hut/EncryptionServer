package com.hut.drivers;

import com.hut.server.Server;

import java.util.HashMap;

public class ServerDriver {

    private static final HashMap<String, String> users = new HashMap<String, String>();

    public static void main(String args[]){


        initializeUsers();

        int portNumber = 16000;

        System.out.println("Starting server...");
        Server s = new Server(portNumber, users);
        s.serve();
        // TODO: Add way to kill server
    }

    private static void initializeUsers(){
        users.put("john_locke", "4815162342108lost");
        users.put("jack_shepard", "we_have_to_go_back");
    }
}

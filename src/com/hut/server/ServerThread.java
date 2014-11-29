package com.hut.server;

import com.hut.Request;
import com.hut.Response;
import com.hut.util.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.*;

/**
 *
 */
public class ServerThread extends Thread{

    private static final Logger log = Logger.getLogger(ServerThread.class.getName());

    private HashMap<String, String> users;
    private Socket socket;
    private boolean authenticated = false;
    private String user = null;
    private ObjectOutputStream oos = null;
    private Handler fh;

    public ServerThread(Socket socket, HashMap<String, String> users){
        super("ServerThread");
        this.socket = socket;
        this.users = users;
        System.out.println("New server created");
        setupLogger();
    }

    private void setupLogger(){
        try{
            new File("./logs").mkdir();
            fh = new FileHandler("logs/server_thread.log");
            log.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        }catch(IOException e){
            // Error setting up logging
        }
        log.setLevel(Level.FINEST);
    }

    public void run(){
        try{
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            Request request;
            Response response = null;

            boolean alive = true;
            FileDownloader fd = new FileDownloader();

            while(alive){

                request = (Request) ois.readObject();
                log.fine(String.format("Request: Username: %s\nmessage: %s\ntype: %s\n",
                        request.getUserName(), request.getMessage(), request.getType().toString()));


                if(authenticated){
                    if(request.isFinish()){
                        finish();
                        // TODO: set response to be yeah I finish now
                        alive = false;
                    }else if(request.isRequest()){
                        // TODO: Decrypt fileName

                        String fileName = request.getMessage();
                        response = fd.getFileResponse(fileName);

                    } else if(request.isAuthenticate()){
                        /*
                        TODO: Should we tell the user they've already authenticated or should we try to log on as the
                        new user
                         */
                        // TODO: at least send user some response

                    }else{
                        // TODO: Ask user what the hell that was, this shouldn't be possible as type is enum
                    }
                }else{
                    authenticated = authorize(request);
                    if(authenticated){
                        // Send user okay
                        response = new Response(Response.OK, Response.AUTHORIZED_MESSAGE);
                    }else{
                        log.fine(String.format("User: %s didn't authenticate", request.getUserName()));
                        // Send them oh no
                        response = new Response(Response.UNAUTHORIZED, Response.UNAUTHORIZED_MESSAGE);
                    }
                }
                sendResponse(response);
            }

            ois.close();
            oos.close();
        }catch(IOException e){

        }catch(ClassNotFoundException e){

        }
        // TODO: do you do the check if ois or oos are not closed here?
    }

    public void sendResponse(Response r){
        try{
            oos.writeObject(r);

        }catch(IOException e){
            log.severe("IO error when sending response\n" + e.getMessage());
        }
    }

    /**
     * Tries to authorize the connecting client
     * @param r request containing authorization details
     * @return true if client successfully authorizes
     */
    public boolean authorize(Request r){
        if(!r.isAuthenticate()){
            return false;
        }

        // TODO: Check decrypt message first
        if(this.users.containsKey(r.getUserName())){
            return true;
        }


        return false;
    }

    /**
     * Cleans up nicely
     */
    public void finish(){

        if(fh != null){
            fh.close();
        }

    }

}

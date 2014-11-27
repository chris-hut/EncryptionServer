package com.hut.server;

import com.hut.Request;
import com.hut.Response;
import com.hut.util.FileDownloader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 */
public class ServerThread extends Thread{

    private HashMap<String, String> users;
    private Socket socket;
    private boolean authenticated = false;
    private String user = null;

    public ServerThread(Socket socket, HashMap<String, String> users){
        super("ServerThread");
        this.socket = socket;
        this.users = users;
    }

    public void run(){
        try{
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Request request;
            Response response;

            boolean alive = true;

            while(alive){

                request = (Request) ois.readObject();

                if(authenticated){
                    if(request.isFinish()){
                        finish();
                        alive = false;
                    }else if(request.isRequest()){
                        FileDownloader fd = new FileDownloader();
                        // TODO: Decrypt fileName

                        /*
                        TODO: Should we check first if the file exists before even calling FileDownloader?
                        Or should we make FileDownloader return a properly formatted response, which either consists
                        of the file or the not found response
                         */

                    } else if(request.isAuthenticate()){
                        /*
                        TODO: Should we tell the user they've already authenticated or should we try to log on as the
                        new user
                         */
                    }else{
                        // TODO: Ask user what the hell that was, this shouldn't be possible as type is enum
                    }
                }else{
                    authenticated = authorize(request);
                    if(authenticated){
                        // User successfully authenticated, let them know this
                        // TODO: Acknowledge users authentication
                    }
                }
            }

            ois.close();
            oos.close();
        }catch(IOException e){

        }catch(ClassNotFoundException e){

        }
        // TODO: do you do the check if ois or oos are not closed here?
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

        // Check if body matches what it should be
        return false;
    }

    /**
     * Cleans up nicely
     */
    public void finish(){

    }

}

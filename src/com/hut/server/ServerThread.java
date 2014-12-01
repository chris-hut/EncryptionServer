package com.hut.server;

import com.hut.Request;
import com.hut.Response;
import com.hut.util.FileDownloader;
import com.hut.util.TeaEncryptionHelper;

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

    private final HashMap<String, String> users;
    private final Socket socket;
    private boolean authenticated = false;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private Handler fh;
    private TeaEncryptionHelper encryptionHelper = null;

    public ServerThread(Socket socket, HashMap<String, String> users){
        super("ServerThread");
        this.socket = socket;
        this.users = users;
        System.out.println("New server created");
        setupLogger();
    }

    private void setupLogger(){
        try{
            // Hack to put logs in log folder
            //noinspection ResultOfMethodCallIgnored
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
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            Request request;
            Response response = null;

            boolean alive = true;
            FileDownloader fd = new FileDownloader();

            while(alive){

                Request unencryptedRequest = (Request) ois.readObject();

                if(authenticated){

                    request = decryptRequest(unencryptedRequest);
                    // TODO: check if can't decrypt properly

                    log.fine("Request: " + request.toString());

                    if(request.isFinish()){
                        response = new Response(Response.OK, Response.CLOSING_SERVER_MESSAGE.getBytes());
                        // Finish is called once loop ends
                        alive = false;
                    }else if(request.isRequest()){
                        // TODO: Decrypt fileName

                        String fileName = request.getMessageString().trim();
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
                    authenticated = authorize(unencryptedRequest);
                    if(authenticated){
                        // Send user okay
                        response = new Response(Response.OK, Response.AUTHORIZED_MESSAGE.getBytes());
                    }else{
                        // Send them oh no
                        response = new Response(Response.UNAUTHORIZED, Response.UNAUTHORIZED_MESSAGE.getBytes());
                    }
                }
                sendResponse(response);
            }

            finish();
        }catch(IOException e){

        }catch(ClassNotFoundException e){

        }
        // TODO: do you do the check if ois or oos are not closed here?
    }

    private void sendResponse(Response r){
        try{
            oos.writeObject(encryptResponse(r));

        }catch(IOException e){
            // User probably disconnected
            log.fine("IO error when sending response, user has likely disconnected");
            // From: http://stackoverflow.com/a/1181325/1684866
            // Once received this, socket is now useless so lets close up everything
            System.out.println("Client has disconnected");
            finish();
        }
    }

    private Response encryptResponse(Response unencryptedResponse){
        log.fine("Sending response: " + unencryptedResponse.toString());
        if(this.encryptionHelper == null){
            // If user didn't authenticate, return an unencrypted response
            return unencryptedResponse;
        }else{
            return new Response(
                // TODO: Should statusCode be encrypted
                // I say it shouldn't otherwise client will not be able to understand that it wasn't able to authenticate
                unencryptedResponse.getStatusCode(),
                this.encryptionHelper.encrypt(unencryptedResponse.getMessage())
            );
        }
    }

    private Request decryptRequest(Request encryptedRequest){
        return new Request(
                this.encryptionHelper.decrypt(encryptedRequest.getMessage()),
                // TODO: Should type be encrypted?
                encryptedRequest.getType()
        );
    }

    /**
     * Tries to authorize the connecting client
     * @param r request containing authorization details
     * @return true if client successfully authorizes
     */
    private boolean authorize(Request r){
        if(!r.isAuthenticate()){
            log.fine("User sent request without first authenticating");
            return false;
        }

        // Go through our list of user-keys checking for a match
        for(String encryptionKey: this.users.values()){
            TeaEncryptionHelper th = new TeaEncryptionHelper(encryptionKey);
            String userName = th.decryptString(r.getMessage());
            if(this.users.containsKey(userName)){
                // We have a match
                String userName1 = userName;
                this.encryptionHelper = th;
                log.fine(String.format("User: %s authenticated successfully", userName));
                return true;
            }
        }

        log.fine(String.format("User provided invalid authentication"));
        return false;
    }

    /**
     * Cleans up nicely
     */
    public void finish(){
        System.out.println("Closing server thread");
        if(fh != null){
            fh.close();
        }
        if(oos != null){
            try{
                oos.close();
            }catch(IOException e){}
        }

        if(ois != null){
            try{
                ois.close();
            }catch(IOException e){}
        }

        if(socket != null){
            try{
                socket.close();
            }catch(IOException e){}
        }

    }

}

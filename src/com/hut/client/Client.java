package com.hut.client;

import com.hut.Request;
import com.hut.Response;

import java.io.*;
import java.net.Socket;
import java.util.logging.*;

/**
 * Client class
 */
public class Client {

    private static final Logger log = Logger.getLogger(Client.class.getName());

    private String userName;
    private String key;
    private String hostName;
    private Socket socket;
    private ObjectOutputStream oos;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private Handler fh;

    public Client(String hostName, String userName, String key){
        this.userName = userName;
        this.key = key;
        this.hostName = hostName;

        setupLogger();
    }

    private void setupLogger(){
        try{
            new File("./logs").mkdir();
            fh = new FileHandler("logs/client.log");
            log.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        }catch(IOException e){
            // error setting up logging
        }
        log.setLevel(Level.FINEST);
    }

    /**
     * Connects to server with hostName
     *
     * @return True if connection is successful
     */
    public boolean connectToServer(int portNumber){
        try{
            this.socket = new Socket(hostName, portNumber);
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.inputStream = socket.getInputStream();
            this.objectInputStream = new ObjectInputStream(this.inputStream);

            String encryptedUsername = userName;
            Request request = new Request(encryptedUsername, Request.AUTHENTICATE, Request.TYPE.AUTHENTICATE);

            System.out.println("Authenticating, waiting for server response");
            log.fine("Authenticating with server");

            Response response = sendRequest(request);

            if(response == null){
                System.out.println("No response from server.");
                log.fine("No response from server");
                return false;
            }

            log.fine(String.format("Response from server: StatusCode: %d\nMessage: %s",
                    response.getStatusCode(), response.getMessage()));

            if(response.getStatusCode() == 200){
                // Successfully authenticated
                // TODO: Check other parameters of response
                System.out.println("Successfully connected to server");
                log.fine("Connected to server");
                return true;
            }

        }catch(IOException e){
            System.out.println("Error connecting to server");
            log.severe("Error connecting to server\n" + e.getMessage());
        }

        return false;
    }

    /**
     * Requests fileName from the server and returns the response from the server
     * @param fileName requested from server
     * @return Response from server
     */
    public Response requestFile(String fileName){
        String encryptedMessage = fileName;

        return sendRequest(new Request(userName, encryptedMessage, Request.TYPE.REQUEST_FILE));
    }


    /**
     * Sends a request to the server and returns the servers response
     * @param r the request to send to the server
     * @return Servers Response
     */
    public Response sendRequest(Request r){
        Response response = null;
        try{
            oos.writeObject(r);

            response = (Response) objectInputStream.readObject();
        }catch(IOException e){
            System.out.println("Error sending request");
            log.fine("Error sending request/n" + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("Error getting response");
            log.fine("Error receiving response/n" + e.getMessage());
        }

        return response;
    }

    /**
     * Closes connection with server
     */
    public void finishConnection(){
        Request request = new Request(userName, Request.FINISH, Request.TYPE.FINISH);
        Response response = sendRequest(request);

        if(response.getStatusCode() == 200){
            log.fine("Server closed successfully");
        }else{
            log.fine("Error closing server");
        }

        try{
            oos.close();
            inputStream.close();
            objectInputStream.close();
        }catch(IOException e){
            // Error closing file
        }

        if(fh != null){
            fh.close();
        }
    }


    public String getUserName(){
        return userName;
    }
}

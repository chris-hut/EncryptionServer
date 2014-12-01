package com.hut.client;

import com.hut.Request;
import com.hut.Response;
import com.hut.util.TeaEncryptionHelper;

import java.io.*;
import java.net.Socket;
import java.util.logging.*;

/**
 * Client class
 */
public class Client {

    private static final Logger log = Logger.getLogger(Client.class.getName());

    private final String userName;
    private final String hostName;
    private ObjectOutputStream oos;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private Handler fh;
    private final TeaEncryptionHelper encryptionHelper;

    public Client(String hostName, String userName, String key){
        this.userName = userName;
        this.hostName = hostName;
        encryptionHelper = new TeaEncryptionHelper(key);

        setupLogger();
    }

    private void setupLogger(){
        try{
            // Hack to put logs in log folder
            //noinspection ResultOfMethodCallIgnored
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
            Socket socket = new Socket(hostName, portNumber);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = socket.getInputStream();
            this.objectInputStream = new ObjectInputStream(this.inputStream);

            Request request = new Request(this.userName.getBytes(), Request.TYPE.AUTHENTICATE);

            System.out.println("Authenticating, waiting for server response");
            log.fine("Authenticating with server");

            Response response = sendRequest(request);

            if(response == null){
                System.out.println("No response from server.");
                log.fine("No response from server");
                return false;
            }

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

        return sendRequest(new Request(fileName.getBytes(), Request.TYPE.REQUEST_FILE));
    }


    /**
     * Sends a request to the server and returns the servers response, performing all encryption/decryption
     * @param r the request to send to the server
     * @return Server's unencrypted response
     */
    private Response sendRequest(Request r){
        Response response = null;
        try{
            log.fine("Sending request: " + r.toString());

            Request encryptedRequest = encryptRequest(r);

            oos.writeObject(encryptedRequest);

            response = decryptResponse((Response) objectInputStream.readObject());
            log.fine("Received response: " + response.toString());
        }catch(IOException e){
            System.out.println("Error sending request");
            log.fine("Error sending request" + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("Error getting response");
            log.fine("Error receiving response/n" + e.getMessage());
        }

        return response;
    }

    private Request encryptRequest(Request r){
        return new Request(
            this.encryptionHelper.encrypt(r.getMessage()),
            r.getType()
        );
    }

    private Response decryptResponse(Response r){
        if(r.getStatusCode() == 401){
            // Unauthorized message, not encrypted
            return r;
        } else{
            return new Response(
                r.getStatusCode(),
               this.encryptionHelper.decrypt(r.getMessage())
            );
        }
    }

    /**
     * Closes connection with server
     */
    public boolean finishConnection(){
        Request request = new Request(Request.FINISH.getBytes(), Request.TYPE.FINISH);
        Response response = sendRequest(request);
        boolean returnValue = false;

        if(response == null){
            log.fine("No response from server");
        }else if(response.getStatusCode() == Response.OK){
            returnValue = true;
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
        return returnValue;
    }


}

package com.hut.drivers;

import com.hut.Request;
import com.hut.Response;
import com.hut.client.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Client Driver class, to be called from the commandline to start a client who can connect to a server
 */
public class ClientDriver {

    public static final String EXIT_STRING = "exit";

    public static void main(String args[]){
        validateArguments(args);

        Client client = new Client(args[0], args[1], args[2]);
        final int portNumber = 16000;

        if(!client.connectToServer(portNumber)){
            System.out.println("Invalid credentials.");
            System.out.println("Exiting...");
            System.exit(0);
        }

        Scanner s = new Scanner(System.in);
        String input = null;
        Response response = null;
        do{
            System.out.print("> ");
            // Not trimming file name since files can have whitespace in them
            input = s.nextLine();
            if(invalidFileNameInput(input)){
                System.out.println(String.format("Invalid input: %s", input));
                displayHelp();
                continue;
            }

            if(input.equalsIgnoreCase("help")){
                displayHelp();
                continue;
            }

            if(input.equalsIgnoreCase("exit")){
                client.finishConnection();
                break;
            }

            response = client.requestFile(input);

            if(response == null){
                System.err.println("Null response from server...");
                continue;
            }

            if(response.getStatusCode() == 200){
                // response is good we'll get the file
                // TODO: Put this in FileDownloader
                String[] fileInfo = response.getMessage().split("\n");
                String fileName = fileInfo[0];
                try{
                    File file = new File(fileName);
                    PrintWriter pw = new PrintWriter(file);
                    System.out.println("Downloading file...");
                    for(int i = 1; i < fileInfo.length; i++){
                        // TODO: Fancy download animation
                        pw.println(fileInfo[i]);
                    }
                    pw.close();
                    System.out.println(String.format("File: %s download complete", fileName));
                }catch(FileNotFoundException e){

                }
            }else if(response.getStatusCode() == 404){
                // File doesn't exist
                System.out.println(String.format("File: %s doesn't exist", input));
            } else{
                // Not sure what this means ehh
            }
        } while(!input.equalsIgnoreCase(EXIT_STRING));
    }

    /**
     * Performs simple check on file input to avoid the expensive check of
     * seeing if the server returns file not found.
     * @param input the users input
     * @return true if input is invalid
     */
    public static boolean invalidFileNameInput(String input){
        if(input.length() == 0){
            return true;
        }

        if(input.trim().length() == 0){
            // file was all whitespace
            return true;
        }

        return false;
    }

    /**
     * Validates the arguments of the program and calls invalidArguments if
     * arguments are invalid*/
    public static void validateArguments(String args[]){
        if(args.length != 3){
            invalidArguments();
        }
    }

    /**
     * Displays help information about how to get files from
     */
    public static void displayHelp(){
        StringBuilder sb = new StringBuilder();
        sb.append("Enter name of file to receive from server or help for this text");
        System.out.println(sb.toString());
    }

    /**
     * Called if arguments are invalid, exits the program after displaying usage message.
     */
    public static void invalidArguments(){
        System.err.println("usage: java ClientDriver <host_name> <user_name> <user_key>");
        System.err.println("\thost_name: the name of the host (server)");
        System.err.println("\tuser_name: the name of the user");
        System.err.println("\tuser_key: specified users key");
        System.exit(1);
    }
}

package com.hut.drivers;

import com.hut.Request;
import com.hut.Response;
import com.hut.client.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Client Driver class, to be called from the commandline to start a client who can connect to a server
 */
public class ClientDriver {

    private static final String EXIT_STRING = "exit";

    public static void main(String args[]){
        validateArguments(args);

        Client client = new Client(args[0], args[1], args[2]);
        final int portNumber = 16000;

        if(!client.connectToServer(portNumber)){
            if(client.connectedToServer(portNumber)){
                System.out.println("Invalid credentials.");
                System.out.println("Exiting...");
            } else{
                System.out.println("Could not connect to server");
            }
            System.exit(0);
        }

        Scanner s = new Scanner(System.in);
        String input;
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
                if(client.finishConnection()){
                    System.out.println("Server closed successfully");
                }else{
                    // Do we even want to say anything here?
                }
                break;
            }

            response = client.requestFile(input);

            if(response == null){
                System.err.println("No response from server");
                continue;
            }

            if(response.getStatusCode() == Response.OK){
                // response is good we'll get the file
                // TODO: Put this in FileDownloader
                String[] fileInfo = response.getMessageString().split("\n");
                String fileName = fileInfo[0];
                try{
                    // TODO: Overwrite?
                    File file = new File(fileName);
                    // Blank file, don't want to add anything to it
                    if(fileInfo.length == 2){
                        file.createNewFile();
                    }else {
                        PrintWriter pw = new PrintWriter(file);
                        System.out.println("Downloading file...");
                        pw.print("");
                        for (int i = 1; i < fileInfo.length; i++) {
                            // TODO: Fancy download animation
                            pw.println(fileInfo[i]);
                        }
                        pw.close();
                    }
                    System.out.println(String.format("File: %s download complete", fileName));
                }catch(FileNotFoundException e){
                    System.out.println("File was not found");
                }catch(IOException e){
                    System.out.println("Error creating file: " + fileName);
                    e.printStackTrace();
                }
            }else if(response.getStatusCode() == Response.FILE_NOT_FOUND){
                // File doesn't exist
                System.out.println(String.format("File: %s doesn't exist", input));
            } else if(response.getStatusCode() == Response.FILE_TOO_LARGE){
                // File was too large to transfer
                System.out.println(String.format("File: %s was too large to transfer", input));
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
    private static boolean invalidFileNameInput(String input){
        if(input.length() == 0){
            return true;
        }

        return input.trim().length() == 0;

    }

    /**
     * Validates the arguments of the program and calls invalidArguments if
     * arguments are invalid*/
    private static void validateArguments(String args[]){
        if(args.length != 3){
            invalidArguments();
        }
    }

    /**
     * Displays help information about how to get files from
     */
    private static void displayHelp(){
        System.out.println("Enter name of file to receive from server or help for this text");
    }

    /**
     * Called if arguments are invalid, exits the program after displaying usage message.
     */
    private static void invalidArguments(){
        System.err.println("usage: java ClientDriver <host_name> <user_name> <user_key>");
        System.err.println("\thost_name: the name of the host (server)");
        System.err.println("\tuser_name: the name of the user");
        System.err.println("\tuser_key: specified users key");
        System.exit(1);
    }
}

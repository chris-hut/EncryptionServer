package com.hut.util;

import com.hut.Response;

import java.io.*;

/**
 * Utility class providing utilities that are used by other classes
 */
public class FileDownloader {

    public Response getFileResponse(String fileName){
        Response response = null;
        System.out.println("Looking for file: " + fileName);
        File f = new File(fileName);
        // TODO: Do something nicer if dealing with directory
        if(f.exists() && f.isFile()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                StringBuilder sb = new StringBuilder();
                sb.append(fileName);
                // Don't want anything added for empty files
                if(f.length() > 0){
                    sb.append("\n");
                    String line = br.readLine();
                    while(line != null){
                        sb.append(line).append("\n");
                        line = br.readLine();
                    }
                }

                // TODO: Encode response message
                String encodedResponse = sb.toString();
                response = new Response(200, encodedResponse.getBytes());
            }catch(FileNotFoundException e){
                // Shouldn't happen as we just checked if file exists
            }catch(IOException e){

            }
        }else{
            // File doesn't exist, return not found response
            response = new Response(404, Response.NOT_FOUND_MESSAGE.getBytes());
        }
        return response;
    }
}

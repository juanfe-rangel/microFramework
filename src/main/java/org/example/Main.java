package org.example;

import org.example.utilities.HttpServer;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.example.utilities.HttpServer.*;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        staticfiles("/webroot");
        get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/App/pi", (req, resp) -> {
            return String.valueOf(Math.PI); 
        });
        
        // Start the server
        HttpServer.main(args);
    }
}
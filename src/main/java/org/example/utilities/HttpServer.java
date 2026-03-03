package org.example.utilities;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    static Map<String, WebMethod> endPoints = new HashMap<>();
    static String staticFilesLocation = "";
    
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080.");
            System.exit(1); }
        Socket clientSocket = null;

        boolean running = true;
        while(running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String reqpath = " ";
            String method = "";
            Request request = null;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if(firstLine){
                    String[] reqTokens = inputLine.split(" ");
                    method = reqTokens[0];
                    String struri = reqTokens[1];
                    String protocol = reqTokens[2];

                    URI requri = new URI(struri);

                    reqpath = requri.getPath();
                    String query = requri.getQuery();

                    System.out.println("Request path: " + reqpath);
                    System.out.println("Query string: " + query);

                    // Create Request object
                    request = new Request(reqpath, method);
                    
                    // Parse query parameters
                    if(query != null) {
                        String[] queryPairs = query.split("&");
                        for(String pair : queryPairs) {
                            String[] keyValue = pair.split("=");
                            if(keyValue.length == 2) {
                                request.setQueryParam(keyValue[0], keyValue[1]);
                            } else if(keyValue.length == 1) {
                                request.setQueryParam(keyValue[0], "");
                            }
                        }
                    }

                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            WebMethod wm = endPoints.get(reqpath);
            
            if(wm != null){
                Response response = new Response();
                String result = wm.execute(request, response);
                
                outputLine =
                        "HTTP/1.1 200 OK\r\n"
                                + "Content-Type: " + response.getContentType() + "\r\n"
                                + "\r\n"
                                + result;
                out.println(outputLine);
            }
            // Check for static files
            else if(!staticFilesLocation.isEmpty()) {
                File staticFile = getStaticFile(reqpath);
                if(staticFile != null && staticFile.exists() && staticFile.isFile()) {
                    serveStaticFile(staticFile, out);
                } else {
                    outputLine =
                            "HTTP/1.1 404 Not Found\r\n"
                                    + "Content-Type: text/html\r\n"
                                    + "\r\n"
                                    + "<!DOCTYPE html>"
                                    + "<html>"
                                    + "<head>"
                                    + "<meta charset=\"UTF-8\">"
                                    + "<title>404 Not Found</title>\n"
                                    + "</head>"
                                    + "<body>"
                                    + "<h1>404 - File Not Found</h1>"
                                    + "</body>"
                                    + "</html>";
                    out.println(outputLine);
                }
            }
            else {
                outputLine =
                        "HTTP/1.1 404 Not Found\r\n"
                                + "Content-Type: text/html\r\n"
                                + "\r\n"
                                + "<!DOCTYPE html>"
                                + "<html>"
                                + "<head>"
                                + "<meta charset=\"UTF-8\">"
                                + "<title>404 Not Found</title>\n"
                                + "</head>"
                                + "<body>"
                                + "<h1>404 - Not Found</h1>"
                                + "</body>"
                                + "</html>";
                out.println(outputLine);
            }

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
    
    public static void get(String path, WebMethod web){
        endPoints.put(path, web);
    }
    
    public static void staticfiles(String location) {
        staticFilesLocation = location;
    }
    
    private static File getStaticFile(String path) {
        try {
            // Get the classes directory path
            String classesPath = HttpServer.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath();
            
            // Build the full path to the static file
            String fullPath = classesPath + staticFilesLocation + path;
            
            fullPath = fullPath.replaceFirst("^/(.:/)", "$1");
            fullPath = java.net.URLDecoder.decode(fullPath, "UTF-8");
            
            File file = new File(fullPath);
            System.out.println("Looking for static file at: " + file.getAbsolutePath());
            
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void serveStaticFile(File file, PrintWriter out) {
        try {
            String contentType = getContentType(file.getName());
            String fileContent = new String(Files.readAllBytes(file.toPath()));
            
            out.print("HTTP/1.1 200 OK\r\n");
            out.print("Content-Type: " + contentType + "\r\n");
            out.print("Content-Length: " + fileContent.length() + "\r\n");
            out.print("\r\n");
            out.print(fileContent);
            out.flush();
            
            System.out.println("Serving static file: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String getContentType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".json")) {
            return "application/json";
        } else {
            return "text/plain";
        }
    }
}
package com.pwojcik;

public class Request {
    private final String request;
    //those two fields right now are not needed, but who knows? I'll leave them for now
    private String verb;
    private String path;

    public Request(String request) {
        this.request = request;
    }


    public String getResourcePath() {
        if (path == null) {
            String requestLine = request.split("\r\n")[0].trim();
            System.out.printf("request line: %s\n", requestLine);
            verb = requestLine.split(" ")[0];
            System.out.printf("verb: %s\n", verb);
            path = requestLine.split(" ")[1].substring(1);
            if (path.isEmpty()) {
                path = "index.html";
            }
        }
        System.out.printf("path: %s\n", path);
        return path;
    }
}

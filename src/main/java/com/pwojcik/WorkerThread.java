package com.pwojcik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkerThread implements Runnable {
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;

    public WorkerThread(Socket client, BufferedReader in, PrintWriter out) {
        this.client = client;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            Request request = new Request(readRequestAsString());
            Path resourcePath = Path.of(request.getResourcePath());
            processResponse(resourcePath);
        } catch (Exception e) {
            //500
            System.out.println(e.getMessage());
            String message = String.format("<H1>Internal server error: %s </h1>", e);
            Response errorResponse = new Response(500, message);
            out.write(errorResponse.asString());

        } finally {
            close();
        }
    }

    private void processResponse(Path resourcePath) throws IOException {
        Response response;
        if (!resourcePath.toFile().exists()) {
            //resource not found on the server - 404
            response = new Response(404, "<H1>Not found</H1>");
        } else {
            //get the file from the filesystem
            String data = Files.readString(resourcePath);
            response = new Response(200, data);
        }

        out.write(response.asString());
    }

    private void close() {
        try {
            out.close();
            in.close();
            client.close();
            System.out.println("Closed");
        } catch (IOException e) {
            System.out.printf("Error while closing socket resources: %s", e.getMessage());
        }

    }

    private String readRequestAsString() throws IOException {
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            result
                    .append(line)
                    .append(System.lineSeparator());
        }
        return result.toString();
    }
}

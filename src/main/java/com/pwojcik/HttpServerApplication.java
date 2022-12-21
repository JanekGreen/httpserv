package com.pwojcik;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerApplication {

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public void start(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.printf("Server started on port: %s\n", port);

            while (true) {
                Socket client = server.accept();
                BufferedReader in = retrieveInputStream(client);
                PrintWriter out = retrieveOutputStream(client);
                System.out.println("Connection established!");
                executor.submit(new WorkerThread(client, in, out));
            }

        } catch (IOException e) {
            //use custom exception here
            throw new RuntimeException(e);
        } finally {
            System.out.println("Shutting down the executor");
            executor.shutdownNow();
        }


    }

    private BufferedReader retrieveInputStream(Socket client) throws IOException {
        return new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    private PrintWriter retrieveOutputStream(Socket client) throws IOException {
        return new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
    }
}

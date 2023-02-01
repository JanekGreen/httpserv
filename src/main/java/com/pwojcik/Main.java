package com.pwojcik;


import java.util.concurrent.Executors;
public class Main {
    public static void main(String[] args) {
        new HttpServerApplication(Executors.newVirtualThreadPerTaskExecutor()).start(8080);
    }
}
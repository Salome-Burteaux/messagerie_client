package org.example;

import org.example.server.ServerController;

public class Main {
    public static void main(String[] args) {
        int port = 12345;

        try {
            System.out.println("Starting server...");
            new ServerController(port).start();
        } catch (Exception e) {
            System.err.println("Server failed: " + e.getMessage());
        }
    }
}

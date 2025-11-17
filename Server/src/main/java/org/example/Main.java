package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Port du serveur
        int p = 12345;
        Server s = new Server(p);
        try {
            System.out.println("Starting server...");
            s.start();
        } catch (IOException e) {
            System.err.println("Server failed: " + e.getMessage());
        }
    }
}
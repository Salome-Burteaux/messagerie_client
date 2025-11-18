package org.example;

import org.example.client.ClientController;

public class Main {
    public static void main(String[] args) {
        try {
            ClientController client = new ClientController("localhost", 12345);
            client.start();
        } catch (Exception e) {
            System.out.println("Client failed: " + e.getMessage());
        }
    }
}

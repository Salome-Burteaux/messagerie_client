package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;

public class MessageReceiver implements Runnable {

    private final BufferedReader reader;

    public MessageReceiver(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                System.out.println("\r" + msg);
                System.out.print("You: ");
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server.");
        }
    }
}

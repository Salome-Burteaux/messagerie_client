package org.example.network;

import java.io.BufferedWriter;
import java.io.IOException;

public class MessageSender {

    private final BufferedWriter writer;

    public MessageSender(BufferedWriter writer) {
        this.writer = writer;
    }

    public void send(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }
}

package org.example.server;

import org.example.network.TcpClientConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientSession implements Runnable {

    private final TcpClientConnection connection;
    private final MessageBroadcaster broadcaster;
    private BufferedReader in;
    private PrintWriter out;

    private String userName;
    private final int clientId;

    public ClientSession(TcpClientConnection connection, MessageBroadcaster broadcaster, int clientId) {
        this.connection = connection;
        this.broadcaster = broadcaster;
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void send(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    @Override
    public void run() {
        try {
            setupStreams();
            requestUserName();
            broadcaster.sendHistoryTo(this);
            broadcastJoinMessage();
            listenForMessages();

        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void setupStreams() throws IOException {
        in = connection.getReader();
        out = connection.getWriter();
    }

    private void requestUserName() throws IOException {
        send("Enter your name:");
        userName = in.readLine();
    }

    private void broadcastJoinMessage() {
        String msg = userName + " has joined the chat.";
        System.out.println(msg);
        broadcaster.broadcast(this, msg);
    }

    private void listenForMessages() throws IOException {
        String text;
        while ((text = in.readLine()) != null) {
            String msg = userName + ": " + text;
            System.out.println(msg);
            broadcaster.broadcast(this, msg);
        }
    }

    private void disconnect() {
        try {
            String msg = userName + " has left the chat.";
            System.out.println(msg);
            broadcaster.broadcast(this, msg);

            connection.close();
            broadcaster.removeClient(this);

        } catch (IOException ignored) {}
    }
}

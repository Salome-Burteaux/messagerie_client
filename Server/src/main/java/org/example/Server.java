package org.example;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    private List<ClientHandler> clientsList = new ArrayList<>();
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private List<String> history = new ArrayList<>();
    private int clientCounter = 0;

    public Server(int port) {

        this.port = port;
    }

    // Starts the server and listens for incoming clients
    public void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", port));
        isRunning = true;

        System.out.println("Chat server started on port " + port);

        while (isRunning) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(clientSocket, clientCounter++);
            clientsList.add(handler);
            new Thread(handler).start();
        }
    }

    public void stop() throws IOException {
        isRunning = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    // Send a message to every connected client (except sender)
    public synchronized void broadcastMessage(ClientHandler sender, String msg) {
        history.add(msg);
        if (history.size() > 100) {
            history.remove(0);
        }

        for (ClientHandler c : clientsList) {
            if (c != sender && c.getUserName() != null) {
                c.sendMessage(msg);
            }
        }
    }

    // Send chat history to a newly connected client
    public void sendHistoryToClient(ClientHandler client) {
        for (String msg : history) {
            client.sendMessage(msg);
        }
    }

    // Inner client handler
    class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String userName;
        private int clientId;

        public ClientHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        public String getUserName() {
            return userName;
        }

        public void sendMessage(String msg) {
            if (out != null) {
                out.println(msg);
            }
        }

        @Override
        public void run() {
            try {
                setupStreams();
                requestUserName();
                sendHistoryToClient(this);
                broadcastJoinMessage();
                listenForMessages();

            } catch (IOException e) {
                System.out.println("Client error: " + e.getMessage());
            } finally {
                disconnect();
            }
        }

        private void setupStreams() throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        }

        private void requestUserName() throws IOException {
            out.println("Enter your name:");
            userName = in.readLine();
        }

        private void broadcastJoinMessage() {
            String msg = userName + " has joined the chat.";
            System.out.println(msg);
            broadcastMessage(this, msg);
        }

        private void listenForMessages() throws IOException {
            String msg;
            while ((msg = in.readLine()) != null) {
                String formatted = userName + ": " + msg;
                System.out.println(formatted);
                broadcastMessage(this, formatted);
            }
        }

        private void disconnect() {
            try {
                String msg = userName + " has left the chat.";
                System.out.println(msg);
                broadcastMessage(this, msg);

                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();

            } catch (IOException ignored) {
            }
        }
    }
}
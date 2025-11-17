package org.example;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    private List<ClientHandler> _clientsList = new ArrayList<>();
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private List<String> hist = new ArrayList<>();
    private int count = 0;

    public Server(int port) {

        this.port = port;
    }

    // crée un serverSocket
    public void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", port));
        isRunning = true;
        System.out.println("Chat server started on port " + port);

        while (isRunning) {
            Socket cs = serverSocket.accept();
            ClientHandler ch = new ClientHandler(cs, this);
            _clientsList.add(ch);
            Thread t = new Thread(ch);
            t.start();
        }
    }

    public void stop() throws IOException {
        isRunning = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    // méthode pour envoyer message à tout le monde
    public void broadcastMessage(ClientHandler sender, String msg) {
        hist.add(msg);
        if (hist.size() > 100) {
            hist.remove(0);
        }

        for (int i = 0; i < _clientsList.size(); i++) {
            ClientHandler c = _clientsList.get(i);
            if (c != sender && c.userName != null) {
                try {
                    c.out.println(msg);
                } catch (Exception e) {
                    // client déconnecté ?
                }
            }
        }
    }

    // envoi historique
    public void sendHistoryToClient(ClientHandler c) {
        for (int i = 0; i < hist.size(); i++) {
            c.out.println(hist.get(i));
        }
    }

    // Classe interne pour gérer chaque client
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
        @Override
        public void run() {
            try {
                setupStreams();
                requestUserName();
                sendHistoryToClient();
                broadcastJoinMessage();
                listenForMessages();

            } catch (IOException e){
                System.out.println("Client error: " + e.getMessage());
            } finally {
                disconnect();
            }

                InputStream in = socket.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                OutputStream outStream = socket.getOutputStream();


                sendHistoryToClient(this);
                broadcastMessage(this, m);

                String receivedMessage;
                while ((receivedMessage = r.readLine()) != null) {
                    m = userName + ": " + receivedMessage;
                    System.out.println(m);
                    broadcastMessage(this, m);
                }

                String msgLeave = userName + " has left the chat.";
                System.out.println(msgLeave);
                broadcastMessage(this, msgLeave);

            } catch (IOException e) {
                System.out.println("Client error");
            }



        private void setupStreams() throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        }

        private void requestUserName() {

            out.println("Enter your name: ");
            userName = in.readLine();
            String m = userName + " has joined the chat.";
            System.out.println(m);
        };

        private void broadcastJoinMessage() {

        };

        private void sendHistoryToClient() {};

        private void listenForMessages() {};

        }
    }
}
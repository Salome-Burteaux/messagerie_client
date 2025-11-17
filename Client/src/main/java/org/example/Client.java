package org.example;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private ExecutorService executor;
    private BufferedReader consoleReader;
    private int messageCount = 0;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    // Connects to the server and starts sending/receiving messages
    public void connect() throws IOException, InterruptedException, ExecutionException {
        openSocketConnection();
        initializeExecutor();

        // Submit the send/receive tasks
        Future<?> receiveFuture = executor.submit(this::receiveMessages);
        Future<?> sendFuture = executor.submit(this::sendMessages);

        // Wait for both threads to finish
        receiveFuture.get();
        sendFuture.get();

        shutdown();
    }

    // Opens a socket connection to the server
    private void openSocketConnection() throws IOException {
        socket = new Socket(serverAddress, serverPort);
    }

    // Initializes a thread pool with 2 threads
    private void initializeExecutor() {
        executor = Executors.newFixedThreadPool(2);
    }

    // Receives messages from the server
    private void receiveMessages() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("\r" + message);
                System.out.print("You: ");
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server.");
        }
    }

    // Sends messages typed in the console to the server
    private void sendMessages() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String input;
            while ((input = consoleReader.readLine()) != null) {
                writer.write(formatMessage(input));
                writer.newLine();
                writer.flush();
                System.out.print("You: ");
                messageCount++;
            }
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    // Closes the executor and socket
    private void shutdown() throws IOException {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    // Trims messages to remove unnecessary whitespace
    private String formatMessage(String msg) {
        return msg.trim();
    }
}

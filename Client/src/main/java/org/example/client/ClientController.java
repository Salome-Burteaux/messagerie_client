package org.example.client;

import org.example.model.Message;
import org.example.network.MessageReceiver;
import org.example.network.MessageSender;
import org.example.network.TcpConnection;
import org.example.utils.ConsoleReader;

import java.io.IOException;
import java.util.concurrent.*;

public class ClientController {

    private final TcpConnection connection;
    private MessageSender sender;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final ConsoleReader consoleReader = new ConsoleReader();

    public ClientController(String addr, int port) {
        this.connection = new TcpConnection(addr, port);
    }

    public void start() throws IOException, ExecutionException, InterruptedException {
        connection.connect();

        sender = new MessageSender(connection.getWriter());
        MessageReceiver receiver = new MessageReceiver(connection.getReader());

        Future<?> receiveTask = executor.submit(receiver);

        Future<?> sendTask = executor.submit(this::handleUserInput);

        receiveTask.get();
        sendTask.get();

        shutdown();
    }

    private void handleUserInput() {
        try {
            String input;
            while ((input = consoleReader.readLine()) != null) {
                Message msg = new Message(input);
                sender.send(msg.getContent());
                System.out.print("You: ");
            }
        } catch (IOException e) {
            System.out.println("Send error: " + e.getMessage());
        }
    }

    private void shutdown() throws IOException {
        executor.shutdown();
        connection.close();
    }
}

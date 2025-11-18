package org.example.server;

import org.example.network.TcpClientConnection;
import org.example.network.TcpServer;

import java.io.IOException;
import java.net.Socket;

public class ServerController {

    private final TcpServer server;
    private final MessageBroadcaster broadcaster = new MessageBroadcaster();
    private int clientCounter = 0;

    public ServerController(int port) {
        this.server = new TcpServer(port);
    }

    public void start() throws IOException {
        server.start();
        while (true) {
            Socket socket = server.acceptClient();
            TcpClientConnection connection = new TcpClientConnection(socket);

            ClientSession session = new ClientSession(connection, broadcaster, clientCounter++);
            broadcaster.registerClient(session);

            new Thread(session).start();
        }
    }
}

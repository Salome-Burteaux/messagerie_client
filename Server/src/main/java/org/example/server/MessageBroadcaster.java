package org.example.server;

import java.util.ArrayList;
import java.util.List;

public class MessageBroadcaster {

    private final List<ClientSession> clients = new ArrayList<>();
    private final ChatHistory history = new ChatHistory();

    public synchronized void registerClient(ClientSession client) {
        clients.add(client);
    }

    public synchronized void removeClient(ClientSession client) {
        clients.remove(client);
    }

    public synchronized void broadcast(ClientSession sender, String msg) {
        history.add(msg);

        for (ClientSession c : clients) {
            if (c != sender && c.getUserName() != null) {
                c.send(msg);
            }
        }
    }

    public void sendHistoryTo(ClientSession client) {
        for (String msg : history.getAll()) {
            client.send(msg);
        }
    }
}

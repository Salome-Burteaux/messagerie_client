package org.example.server;

import java.util.ArrayList;
import java.util.List;

public class ChatHistory {

    private final List<String> history = new ArrayList<>();

    public synchronized void add(String msg) {
        history.add(msg);
        if (history.size() > 100) {
            history.remove(0);
        }
    }

    public synchronized List<String> getAll() {
        return new ArrayList<>(history);
    }
}

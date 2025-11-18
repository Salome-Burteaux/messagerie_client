package org.example.model;

public class Message {
    private final String content;

    public Message(String content) {
        this.content = content.trim();
    }

    public String getContent() {
        return content;
    }
}

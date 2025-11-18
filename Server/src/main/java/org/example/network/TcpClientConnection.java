package org.example.network;

import java.io.*;
import java.net.Socket;

public class TcpClientConnection {

    private final Socket socket;

    public TcpClientConnection(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}

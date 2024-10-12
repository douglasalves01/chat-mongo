package org.example;

import java.net.*;
import java.io.*;

public class ChatUser {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public ChatUser(String username, String host, int port) throws IOException {
        this.username = username;
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        writer.println(username + ": " + message);
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
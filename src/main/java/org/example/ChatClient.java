package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ChatClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient("localhost", 8000);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Conectado ao chat. Digite suas mensagens:");

            new Thread(() -> {
                try {
                    String message;
                    while ((message = client.receiveMessage()) != null) {
                        System.out.println("Outro: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("sair")) {
                    client.close();
                    break;
                }
                client.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
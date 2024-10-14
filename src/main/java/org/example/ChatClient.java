package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.config.MongoConnect;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String name;

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

            System.out.print("Digite seu nome: ");
            String name = scanner.nextLine();
            client.name = name; // Armazena o nome do usuário

            System.out.println("Conectado ao chat. Digite suas mensagens:");
            MongoCollection<Document> collection = MongoConnect.connectionDB();

            // Thread para receber mensagens
            new Thread(() -> {
                try {
                    String message;
                    while ((message = client.receiveMessage()) != null) {
                        System.out.println(message); // Exibe mensagens recebidas

                        Document doc = new Document("message", message);
                        collection.insertOne(doc);
                    }
                } catch (IOException e) {
                    System.err.println("Erro de I/O: " + e.getMessage());
                }
            }).start();

            // Lógica para enviar mensagens
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("sair")) {
                    client.close();
                    break;
                }
                // Envia a mensagem com o nome do usuário
                String fullMessage = name + ": " + message;
                client.sendMessage(fullMessage);

                Document doc = new Document("message", fullMessage);
                collection.insertOne(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

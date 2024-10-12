package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static Socket client1;
    private static Socket client2;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Servidor iniciado. Aguardando conexões...");

            // Aceitar duas conexões de clientes
            client1 = serverSocket.accept();
            System.out.println("Cliente 1 conectado.");
            client2 = serverSocket.accept();
            System.out.println("Cliente 2 conectado.");

            // Criar threads para gerenciar a comunicação
            new Thread(new ClientHandler(client1, client2)).start();
            new Thread(new ClientHandler(client2, client1)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Socket otherClientSocket;

    public ClientHandler(Socket clientSocket, Socket otherClientSocket) {
        this.clientSocket = clientSocket;
        this.otherClientSocket = otherClientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(otherClientSocket.getOutputStream(), true)) {

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Mensagem recebida: " + message);
                writer.println(message); // Enviar a mensagem para o outro cliente
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.ntf.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private int port;
    private boolean running = false;
    private List<ClientHandler> clients;
    private LobbyManager lobbyManager;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.lobbyManager = new LobbyManager();
    }

    public void start() {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ClientHandler handler = new ClientHandler(clientSocket, this);
                clients.add(handler);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }
}

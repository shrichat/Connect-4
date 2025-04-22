package com.ntf.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            out.println("Enter your username:");
            username = in.readLine();
            System.out.println("User connected: " + username);

            sendLobbyStatus();

            while (true) {
                String input = in.readLine();
                if (input == null) break;

                System.out.println(username + ": " + input);

                if (input.equals("REQUEST_LOBBY_STATUS")) {
                    sendLobbyStatus();
                }

                if (input.startsWith("JOIN_LOBBY:")) {
                    int lobbyId = Integer.parseInt(input.split(":")[1]);
                    Lobby lobby = server.getLobbyManager().getLobby(lobbyId);

                    boolean joined = lobby.addPlayer(this);
                    if (joined) {
                        out.println("JOIN_SUCCESS:" + lobbyId + ":" + lobby.getCurrentSize());
                        System.out.println(username + " joined Lobby " + lobbyId);
                    } else {
                        out.println("JOIN_FAILED:Lobby full");
                    }

                    if (lobby.isFull()) {
                        ClientHandler player1 = lobby.getPlayer1();
                        ClientHandler player2 = lobby.getPlayer2();

                        player1.getWriter().println("COLOR_ASSIGN:RED");
                        player2.getWriter().println("COLOR_ASSIGN:YELLOW");

                        player1.getWriter().println("LOBBY_READY:" + player2.getUsername());
                        player2.getWriter().println("LOBBY_READY:" + player1.getUsername());
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Client " + username + " disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendLobbyStatus() {
        StringBuilder status = new StringBuilder("LOBBY_STATUS:");
        for (int i = 1; i <= 3; i++) {
            Lobby lobby = server.getLobbyManager().getLobby(i);
            status.append(lobby.getCurrentSize()).append("/2");
            if (i < 3) status.append(",");
        }
        out.println(status.toString());
    }

    public PrintWriter getWriter() {
        return out;
    }

    public String getUsername() {
        return username;
    }
}

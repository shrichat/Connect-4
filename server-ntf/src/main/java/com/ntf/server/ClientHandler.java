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

            while (true) {
                String input = in.readLine();
                if (input == null) break;

                System.out.println(username + ": " + input);

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
                        ClientHandler opponent = lobby.getOpponent(this);
                        if (opponent != null) {
                            opponent.getWriter().println("LOBBY_READY:" + username);
                            out.println("LOBBY_READY:" + opponent.getUsername());
                        }
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

    public PrintWriter getWriter() {
        return out;
    }

    public String getUsername() {
        return username;
    }
}

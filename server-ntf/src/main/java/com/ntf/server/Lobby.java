package com.ntf.server;

public class Lobby {
    private int id;
    private ClientHandler player1;
    private ClientHandler player2;
    private boolean playAgain1 = false;
    private boolean playAgain2 = false;

    public Lobby(int id) {
        this.id = id;
    }

    public synchronized boolean addPlayer(ClientHandler player) {
        if (player1 != null && player1.getUsername().equals(player.getUsername())) return false;
        if (player2 != null && player2.getUsername().equals(player.getUsername())) return false;

        if (player1 == null) {
            player1 = player;
            return true;
        } else if (player2 == null) {
            player2 = player;
            return true;
        }

        return false;
    }

    public synchronized void removePlayer(ClientHandler player) {
        if (player == player1) {
            player1 = null;
        } else if (player == player2) {
            player2 = null;
        }
    }

    public boolean isFull() {
        return player1 != null && player2 != null;
    }

    public int getCurrentSize() {
        int size = 0;
        if (player1 != null) size++;
        if (player2 != null) size++;
        return size;
    }

    public ClientHandler getOpponent(ClientHandler player) {
        if (player == player1) return player2;
        if (player == player2) return player1;
        return null;
    }

    public int getId() {
        return id;
    }

    public ClientHandler getPlayer1() {
        return player1;
    }

    public ClientHandler getPlayer2() {
        return player2;
    }
    
    public synchronized void handlePlayAgain(ClientHandler player) {
        if (player == player1) {
            playAgain1 = true;
        } else if (player == player2) {
            playAgain2 = true;
        }

        if (playAgain1 && playAgain2 && isFull()) {
            player1.getWriter().println("PLAY_AGAIN_ACCEPTED");
            player2.getWriter().println("PLAY_AGAIN_ACCEPTED");
            playAgain1 = false;
            playAgain2 = false;
        }
    }

}

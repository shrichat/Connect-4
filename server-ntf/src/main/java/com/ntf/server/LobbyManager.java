package com.ntf.server;

import java.util.HashMap;
import java.util.Map;

public class LobbyManager {
    private Map<Integer, Lobby> lobbies;

    public LobbyManager() {
        lobbies = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            lobbies.put(i, new Lobby(i));
        }
    }

    public Lobby getLobby(int id) {
        return lobbies.get(id);
    }

    public Map<Integer, Lobby> getAllLobbies() {
        return lobbies;
    }
}

package com.ntf.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public boolean connect(String host, int port, String username) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String prompt = in.readLine();
            System.out.println("Server: " + prompt);

            out.println(username);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PrintWriter getWriter() {
        return out;
    }

    public BufferedReader getReader() {
        return in;
    }

    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

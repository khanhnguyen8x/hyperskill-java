package com.khanhnv.fileserver2.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private static final int PORT = 34522;

    public static void main(String[] args) {
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(PORT)) {
            boolean running = true;
            while (running) {
                Session session = new Session(server.accept());
                session.run();
                running = !session.isExit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


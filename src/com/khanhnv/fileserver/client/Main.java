package com.khanhnv.fileserver.client;

public class Main {
    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 34522;

    public static void main(String[] args) {
        var client = new Client();
        client.start();
    }
}


package com.khanhnv.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 34522;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            for (int i = 0; i < 5; i++) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();

                output.writeUTF(msg);
                String receivedMsg = input.readUTF();

                System.out.println(receivedMsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.khanhnv.fileserver.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class Client {

    private String action;
    private String fileName;
    private String fileContent;

    void start() {
        var scanner = new Scanner(System.in);
        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
        var action = scanner.nextLine().trim();
        if (action.equalsIgnoreCase("exit")) {
            exit();
            return;
        }
        System.out.print("Enter filename:");
        var fileName = scanner.nextLine().trim();
        String fileContent = null;
        if (action.equalsIgnoreCase("2")) {
            System.out.print("Enter file content:");
            fileContent = scanner.nextLine().trim();
        }
        this.action = action;
        this.fileName = fileName;
        this.fileContent = fileContent;
        doAction();
    }

    private void doAction() {
        switch (action) {
            case "1":
                getFile();
                break;
            case "2":
                createFile();
                break;
            case "3":
                deleteFile();
                break;
            case "exit":
                break;
            default:
                break;
        }
    }

    void exit() {
        makeRequest("exit");
    }

    void getFile() {
        var request = buildRequest("GET", fileName, fileContent);
        makeRequest(request);
    }

    void createFile() {
        var request = buildRequest("PUT", fileName, fileContent);
        makeRequest(request);
    }

    void deleteFile() {
        var request = buildRequest("DELETE", fileName, fileContent);
        makeRequest(request);
    }

    String buildRequest(String action, String fileName, String fileContent) {
        var builder = new StringBuilder();
        switch (action) {
            case "GET":
                return builder.append("GET").append(" ").append(fileName).toString();
            case "PUT":
                return builder.append("PUT").append(" ").append(fileName).append(" ").append(fileContent).toString();
            case "DELETE":
                return builder.append("DELETE").append(" ").append(fileName).toString();
            default:
                break;
        }
        return null;
    }

    void makeRequest(String request) {
        try (
                Socket socket = new Socket(Main.SERVER_ADDRESS, Main.SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeUTF(request);
            System.out.println("The request was sent.");
            if(!request.equalsIgnoreCase("exit")) {
                String receivedMsg = input.readUTF();
                parseResponse(receivedMsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void parseResponse(String message) {
        var status = "";
        var content = "";
        String[] args = message.split(" ");
        if (args.length == 1) {
            status = args[0];
        } else if (args.length >= 2) {
            status = args[0];
            content = message.substring(status.length() + 1);
        }

        var success = status.equals("200");

        switch (action) {
            case "1":
                if (success) {
                    System.out.println("The content of the file is: " + content);
                } else {
                    System.out.println("The response says that the file was not found!");
                }
                break;
            case "2":
                if (success) {
                    System.out.println("The response says that the file was created!");
                } else {
                    System.out.println("The response says that creating the file was forbidden!");
                }
                break;
            case "3":
                if (success) {
                    System.out.println("The response says that the file was successfully deleted!");
                } else {
                    System.out.println("The response says that the file was not found!");
                }
                break;
            default:
                break;
        }
    }
}
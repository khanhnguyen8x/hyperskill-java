package com.khanhnv.fileserver2.client;

import util.FileUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Client {

    private String action;
    public static final String PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;

    //    private static final String PATH = "/Users/khanhnv/Documents/GitHub/File Server/File Server/task/src/client/data";
    static {
        try {
            Files.createDirectories(Path.of(PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void start() {
        var scanner = new Scanner(System.in);
        print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file):");
        this.action = scanner.nextLine().trim();
        doAction(scanner);
    }

    private void doAction(Scanner scanner) {
        switch (action) {
            case "1":
                getFile(scanner);
                break;
            case "2":
                createFile(scanner);
                break;
            case "3":
                deleteFile(scanner);
                break;
            case "exit":
                exit();
                break;
            default:
                break;
        }
    }

    void exit() {
        makeExitRequest("exit");
    }

    void getFile(Scanner scanner) {
        print("Do you want to get the file by name or by id (1 - name, 2 - id):");
        var cmd = scanner.nextLine().trim();
        String content;
        String request;
        switch (cmd) {
            case "1":
                print("Enter name:");
                content = scanner.nextLine().trim();
                request = "GET NAME " + content;
                makeGetRequest(scanner, request);
                break;
            case "2":
                print("Enter id:");
                content = scanner.nextLine().trim();
                request = "GET ID " + content;
                makeGetRequest(scanner, request);
                break;
            default:
                break;
        }
    }

    void createFile(Scanner scanner) {
        print("Enter name of the file:");
        var fileName = scanner.nextLine().trim();
        print("Enter name of the file to be saved on server:");
        var fileNameServer = scanner.nextLine().trim();
        if (fileNameServer.length() == 0) {
            fileNameServer = fileName;
        }
        var request = "PUT" + " " + fileNameServer;
        makePutRequest(request, fileName);
    }

    void deleteFile(Scanner scanner) {
        print("Do you want to delete the file by name or by id (1 - name, 2 - id):");
        var cmd = scanner.nextLine().trim();
        String content;
        String request;
        switch (cmd) {
            case "1":
                print("Enter name:");
                content = scanner.nextLine().trim();
                request = "DELETE NAME " + content;
                makeDeleteRequest(scanner, request);
                break;
            case "2":
                print("Enter id:");
                content = scanner.nextLine().trim();
                request = "DELETE ID " + content;
                makeDeleteRequest(scanner, request);
                break;
            default:
                break;
        }
    }

    void makeExitRequest(String request) {
        byte[] message = request.getBytes();
        try (
                Socket socket = new Socket(Main.SERVER_ADDRESS, Main.SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeInt(message.length);
            output.write(message);
            println("The request was sent.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void makeGetRequest(Scanner scanner, String request) {
        try (
                Socket socket = new Socket(Main.SERVER_ADDRESS, Main.SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            var length = request.length();
            output.writeInt(length);
            output.writeInt(request.length());
            output.write(request.getBytes());
            output.write(new byte[]{});
            println("The request was sent.");
            handleGetResponse(scanner, input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void makePutRequest(String request, String fileName) {
        try (
                Socket socket = new Socket(Main.SERVER_ADDRESS, Main.SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            Path path = Path.of(PATH, fileName);
            byte[] bytes = Files.readAllBytes(path);
            var length = request.length() + bytes.length;
            output.writeInt(length);
            output.writeInt(request.length());
            output.write(request.getBytes());
            output.write(bytes);
            println("The request was sent.");

            handlePutResponse(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void makeDeleteRequest(Scanner scanner, String request) {
        try (
                Socket socket = new Socket(Main.SERVER_ADDRESS, Main.SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            var length = request.length();
            output.writeInt(length);
            output.writeInt(request.length());
            output.write(request.getBytes());
            output.write(new byte[]{});
            println("The request was sent.");
            handleDeleteResponse(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePutResponse(DataInputStream input) throws IOException {
        int length = input.readInt();
        int requestLength = input.readInt();
        byte[] request = new byte[requestLength];
        input.read(request, 0, request.length);
        int imageLength = length - requestLength;
        byte[] content = new byte[imageLength];
        input.read(content, 0, content.length);

        var success = new String(request).contains("200");

        if (success) {
            println("Response says that file is saved! ID = " + new String(request).split("\\s")[1]);
        } else {
            println("The response says that creating the file was forbidden!");
        }
    }

    private void handleGetResponse(Scanner scanner, DataInputStream input) throws IOException {
        int length = input.readInt();
        int requestLength = input.readInt();
        byte[] request = new byte[requestLength];
        input.read(request, 0, request.length);
        int imageLength = length - requestLength;
        byte[] content = new byte[imageLength];
        input.read(content, 0, content.length);

        var success = new String(request).equals("200");

        if (success) {
            print("The file was downloaded! Specify a name for it:");
            var name = scanner.nextLine().trim();
            FileUtil.doCreateFile(PATH, name, content);
            println("File saved on the hard drive!");
        } else {
            println("The response says that this file is not found!");
        }
    }

    private void handleDeleteResponse(DataInputStream input) throws IOException {
        int length = input.readInt();
        int requestLength = input.readInt();
        byte[] request = new byte[requestLength];
        input.read(request, 0, request.length);
        int imageLength = length - requestLength;
        byte[] content = new byte[imageLength];
        input.read(content, 0, content.length);
        var success = new String(request).contains("200");

        if (success) {
            println("The response says that this file was deleted successfully!");
        } else {
            println("The response says that this file is not found!");
        }
    }

    private void print(String message) {
        System.out.print(message);
    }

    private void println(String message) {
        System.out.println(message);
    }
}
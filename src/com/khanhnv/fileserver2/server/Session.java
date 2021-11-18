package com.khanhnv.fileserver2.server;

import util.FileUtil;
import util.IdUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Session extends Thread {
    private final Socket socket;
    private String action;
    private String option;
    private String fileName;
    private String fileId;
    private boolean exit;

    public static final String PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;
//    public static final String PATH = "/Users/khanhnv/Documents/GitHub/File Server/File Server/task/src/server/data";

    static {
        try {
            Files.createDirectories(Path.of(PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session(Socket socketForClient) {
        this.socket = socketForClient;
    }

    @Override
    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            int length = input.readInt();
            int requestLength = input.readInt();
            byte[] request = new byte[requestLength];
            input.read(request, 0, request.length);
            int imageLength = length - requestLength;
            byte[] content = new byte[imageLength];
            input.read(content, 0, content.length);
            doAction(new String(request), content, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doAction(String request, byte[] content, DataOutputStream output) throws IOException {
        String[] args = request.split("\\s");
        if (args.length == 1) {
            this.action = args[0];
        } else if (args.length == 2) {
            this.action = args[0];
            this.fileName = args[1];
        } else if (args.length == 3) {
            this.action = args[0];
            this.option = args[1];
            if (option.equals("ID")) {
                this.fileId = args[2];
            } else if (option.equals("NAME")) {
                this.fileName = args[2];
            }
        }

        switch (action) {
            case "GET":
                getFile(output);
                break;
            case "PUT":
                createFile(fileName, content, output);
                break;
            case "DELETE":
                deleteFile(output);
                break;
            case "exit":
                exit();
                break;
            default:
                break;
        }
    }

    private void exit() {
        exit = true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isExit() {
        return exit;
    }

    private void deleteFile(DataOutputStream outputStream) throws IOException {
        String name = null;
        if (option.equals("ID")) {
            name = IdUtil.getMap().get(this.fileId);
        } else if (option.equals("NAME")) {
            name = fileName;
        }
        Path path = Path.of(PATH, name);
        var file = path.toFile();
        var result = false;
        if (file.exists()) {
            result = file.delete();
        }
        if (result) {
            var mapIds = IdUtil.getMap();
            mapIds.remove(fileId);
            IdUtil.saveMap(mapIds);
        }
        var response = result ? "200 " : "404";
        byte[] bytes = new byte[]{};
        var length = response.length() + bytes.length;
        outputStream.writeInt(length);
        outputStream.writeInt(response.length());
        outputStream.write(response.getBytes());
        outputStream.write(bytes);
    }

    private void getFile(DataOutputStream outputStream) throws IOException {
        String name = null;
        if (option.equals("ID")) {
            name = IdUtil.getMap().get(this.fileId);
        } else if (option.equals("NAME")) {
            name = fileName;
        }
        if (null == name || name.length() == 0) {
            var response = "404";
            byte[] bytes = new byte[]{};
            var length = response.length() + bytes.length;
            outputStream.writeInt(length);
            outputStream.writeInt(response.length());
            outputStream.write(response.getBytes());
            outputStream.write(bytes);
            return;
        }
        Path path = Path.of(PATH, name);
        var file = path.toFile();
        if (file.exists()) {
            var response = "200";
            byte[] bytes = Files.readAllBytes(path);
            var length = response.length() + bytes.length;
            outputStream.writeInt(length);
            outputStream.writeInt(response.length());
            outputStream.write(response.getBytes());
            outputStream.write(bytes);
        } else {
            var response = "404";
            byte[] bytes = new byte[]{};
            var length = response.length() + bytes.length;
            outputStream.writeInt(length);
            outputStream.writeInt(response.length());
            outputStream.write(response.getBytes());
            outputStream.write(bytes);
        }
    }

    private void createFile(String fileName, byte[] content, DataOutputStream outputStream) throws IOException {
        var result = FileUtil.doCreateFile(PATH, fileName, content);
        String id = String.valueOf(System.currentTimeMillis());
        if (result) {
            var mapIds = IdUtil.getMap();
            mapIds.put(id, fileName);
            IdUtil.saveMap(mapIds);
        }
        var response = result ? "200" + " " + id : "403";
        byte[] bytes = new byte[]{};
        var length = response.length() + bytes.length;
        outputStream.writeInt(length);
        outputStream.writeInt(response.length());
        outputStream.write(response.getBytes());
        outputStream.write(bytes);
    }
}
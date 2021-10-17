package com.khanhnv.fileserver.server;

import java.io.*;
import java.net.Socket;

class Session extends Thread {
    private final Socket socket;
    private String action;
    private String fileName;
    private String fileContent;
    private boolean exit;

    public Session(Socket socketForClient) {
        this.socket = socketForClient;
    }

    @Override
    public void run() {
        var path = getCurrentPath();
        var parent = new File(path);
        parent.mkdirs();

        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String message = input.readUTF();
            doAction(message, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseMessage(String message) {
        String[] args = message.split(" ");
        if (args.length == 1) {
            this.action = args[0];
        } else if (args.length == 2) {
            this.action = args[0];
            this.fileName = args[1];
        } else if (args.length >= 3) {
            this.action = args[0];
            this.fileName = args[1];
            var begin = this.action.length() + this.fileName.length() + 2;
            this.fileContent = message.substring(begin);
        }
    }

    private void doAction(String message, DataOutputStream output) {
        parseMessage(message);
        switch (action) {
            case "GET":
                getFile(output);
                break;
            case "PUT":
                createFile(output);
                break;
            case "DELETE":
                deleteFile(output);
                break;
            case "exit":
                exitSession();
                break;
            default:
                break;
        }
    }

    private void exitSession() {
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

    private void deleteFile(DataOutputStream outputStream) {
        var result = doDeleteFile();
        var response = result ? "200 " : "404";
        try {
            outputStream.writeUTF(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean doDeleteFile() {
        var path = getCurrentPath();
        var parent = new File(path);
        parent.mkdirs();
        var file = new File(parent, this.fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private void getFile(DataOutputStream outputStream) {
        var result = doGetFile();
        var response = result != null ? "200 " + result : "404";
        try {
            outputStream.writeUTF(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String doGetFile() {
        var path = getCurrentPath();
        var parent = new File(path);
        parent.mkdirs();
        var file = new File(parent, this.fileName);
        String content = null;
        if (file.exists()) {
            try (InputStream inputStream = new FileInputStream(file)) {
                content = new String(inputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }
        return null;
    }

    private void createFile(DataOutputStream outputStream) {
        var result = doCreateFile();
        var response = result ? "200" : "403";
        try {
            outputStream.writeUTF(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean doCreateFile() {
        var path = getCurrentPath();
        var parent = new File(path);
        parent.mkdirs();
        var file = new File(parent, this.fileName);
        var createFile = false;
        try {
            createFile = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (createFile) {
            try (OutputStream outputStream = new FileOutputStream(file.getAbsolutePath(), false)) {
                outputStream.write(fileContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    private String getCurrentPath() {
        return "./src/server/data";
    }
}
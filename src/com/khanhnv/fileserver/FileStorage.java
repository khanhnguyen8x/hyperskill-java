package com.khanhnv.fileserver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileStorage {

    private List<String> fileNames = new ArrayList<>();
    private List<String> fileCreates = new ArrayList<>();

    FileStorage() {
        for (int i = 1; i < 11; i++) {
            fileNames.add("file" + i);
        }
    }

    public void input() {
        var scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) {
                break;
            } else {
                String[] cmd = line.split(" ");
                if (cmd.length == 2) {
                    var command = cmd[0];
                    var name = cmd[1];
                    switch (command) {
                        case "add":
                            add(name);
                            break;
                        case "get":
                            get(name);
                            break;
                        case "delete":
                            delete(name);
                            break;
                    }
                }
            }
        }
    }

    private String getCurrentPath() {
        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toString();
    }

    private void delete(String name) {
        /*
        var parent = new File(getCurrentPath());
        var file = new File(parent, name);
        if (file.delete()) {
            System.out.printf("The file %s was deleted\n", name);
        } else {
            System.out.printf("The file %s not found\n", name);
        }
         */
        if (fileCreates.contains(name)) {
            fileCreates.remove(name);
            System.out.printf("The file %s was deleted\n", name);
        } else {
            System.out.printf("The file %s not found\n", name);
        }
    }

    private void get(String name) {
        /*
        var parent = new File(getCurrentPath());
        var file = new File(parent, name);
        if (file.exists()) {
            System.out.printf("The file %s was sent\n", name);
        } else {
            System.out.printf("The file %s not found\n", name);
        }
         */
        if (fileCreates.contains(name)) {
            System.out.printf("The file %s was sent\n", name);
        } else {
            System.out.printf("The file %s not found\n", name);
        }
    }

    private boolean add(String name) {
        if (!fileNames.contains(name)) {
            System.out.printf("Cannot add the file %s\n", name);
            return false;
        }
        if (!fileCreates.contains(name)) {
            fileCreates.add(name);
            System.out.printf("The file %s added successfully\n", name);
            return true;
        } else {
            System.out.printf("Cannot add the file %s\n", name);
            return false;
        }
        /*
        var parent = new File(getCurrentPath());
        var file = new File(parent, name);

        try {
            var result = file.createNewFile();
            if (result) {
                System.out.printf("The file %s added successfully\n", name);
                return true;
            } else {
                System.out.printf("Cannot add the file %s\n", name);
                return false;
            }
        } catch (IOException e) {
            System.out.printf("Cannot add the file %s\n", name);
            return false;
        }
         */
    }
}

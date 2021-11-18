package com.khanhnv.fileserver2.util;

import client.Client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

    public static void createFiles(String clientDataPath) {

        for (int i = 0; i < 2; i++) {
            try {
                File file = new File(clientDataPath + String.format("test_purpose_test%d.txt", i + 1));
                if (!file.exists()) file.createNewFile();
                FileWriter writer = new FileWriter(file, false);
                writer.write(String.format("test%d", i + 1));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException("Can't create test files!");
            }

        }
    }

    public static void main(String[] args) {
        var clientDataPath = Client.PATH;
        createFiles(clientDataPath);
    }
}

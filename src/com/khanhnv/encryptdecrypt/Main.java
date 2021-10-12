package com.khanhnv.encryptdecrypt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        var mode = "";
        var key = "";
        var data = "";
        var in = "";
        var out = "";
        var alg = "";
        var printData = false;

        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equalsIgnoreCase("-mode")) {
                mode = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-key")) {
                key = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-data")) {
                data = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-in")) {
                in = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-out")) {
                out = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-alg")) {
                alg = args[i + 1];
            }
        }

        if (isEmpty(mode)) {
            mode = "enc";
        }

        if (isEmpty(key)) {
            key = "0";
        }

        if (isEmpty(alg)) {
            alg = "shift";
        }

        String processData = null;
        if (isEmpty(data) && isEmpty(in)) {
            processData = "";
        } else if (!isEmpty(data)) {
            processData = data;
        } else if (!isEmpty(in)) {
            try {
                processData = readFileAsString(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (isEmpty(out)) {
            printData = true;
        }

        var keyNum = Integer.parseInt(key);

        var result = "";
        result = AlgorithmFactory.getAlgorithm(mode, alg).format(processData, keyNum);

        if (printData) {
            System.out.println(result);
        } else {
            writeFile(out, result);
        }
    }


    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void writeFile(String fileName, String content) {
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.print(content);
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }
    }
}

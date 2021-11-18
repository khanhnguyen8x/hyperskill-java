package com.khanhnv.fileserver2.util;

import server.Session;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IdUtil {

    public static Map<String, String> getMap() throws IOException {
        var path = Session.PATH;
        var parent = new File(path);
        String fileName = "map.txt";
        var file = new File(parent, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            HashMap<String, String> mapInFile = (HashMap<String, String>) ois.readObject();
            ois.close();
            fis.close();
            return mapInFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static boolean saveMap(Map<String, String> ids) {
        var path = Session.PATH;
        var parent = new File(path);
        String fileName = "map.txt";
        var file = new File(parent, fileName);
        if (!file.exists()) {
            var createFile = false;
            try {
                createFile = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ids);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}

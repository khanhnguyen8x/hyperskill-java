package com.khanhnv.fileserver2.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {

    public static boolean doCreateFile(String path, String fileName, byte[] content) {
        var parent = new File(path);
        parent.mkdirs();
        var file = new File(parent, fileName);
        var createFile = false;
        try {
            createFile = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (createFile) {
            try (OutputStream outputStream = new FileOutputStream(file.getAbsolutePath(), false)) {
                outputStream.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }
}

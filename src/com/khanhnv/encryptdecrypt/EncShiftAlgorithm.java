package com.khanhnv.encryptdecrypt;

public class EncShiftAlgorithm implements Algorithm {

    @Override
    public String format(String message, int key) {
        var builder = new StringBuilder();
        char[] chars = message.toCharArray();

        char cypher;
        for (var i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                if (chars[i] + key > 'z') {
                    cypher = (char) ('a' + key - 'z' + chars[i] - 1);
                } else {
                    cypher = (char) (chars[i] + key);
                }
                builder.append(cypher);
            } else if (chars[i] >= 'A' && chars[i] <= 'Z') {
                if (chars[i] + key > 'Z') {
                    cypher = (char) ('A' + key - 'Z' + chars[i] - 1);
                } else {
                    cypher = (char) (chars[i] + key);
                }
                builder.append(cypher);
            } else {
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }
}

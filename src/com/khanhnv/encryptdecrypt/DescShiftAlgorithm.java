package com.khanhnv.encryptdecrypt;

public class DescShiftAlgorithm implements Algorithm {

    @Override
    public String format(String message, int key) {
        var builder = new StringBuilder();
        char[] chars = message.toCharArray();
        char cypher;

        for (var i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                if (chars[i] - key < 'a') {
                    cypher = (char) ('z' - ('a' - chars[i] + key) + 1);
                } else {
                    cypher = (char) (chars[i] - key);
                }
                builder.append(cypher);
            } else if (chars[i] >= 'A' && chars[i] <= 'Z') {
                if (chars[i] - key < 'A') {
                    cypher = (char) ('Z' - ('A' - chars[i] + key) + 1);
                } else {
                    cypher = (char) (chars[i] - key);
                }
                builder.append(cypher);
            } else {
                builder.append(chars[i]);
            }
        }

        return builder.toString();
    }
}

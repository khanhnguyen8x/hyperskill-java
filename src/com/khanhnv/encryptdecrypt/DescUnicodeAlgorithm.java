package com.khanhnv.encryptdecrypt;

public class DescUnicodeAlgorithm implements Algorithm {

    @Override
    public String format(String message, int key) {
        var builder = new StringBuilder();
        char[] chars = message.toCharArray();
        char cypher;
        for (var i = 0; i < chars.length; i++) {
            cypher = (char) (chars[i] - key);
            builder.append(cypher);

        }
        return builder.toString();
    }
}

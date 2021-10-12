package com.khanhnv.encryptdecrypt;

public class AlgorithmFactory {

    public static Algorithm getAlgorithm(String mode, String name) {
        if (mode.equalsIgnoreCase("enc")) {
            if ("unicode".equals(name)) {
                return new EncUnicodeAlgorithm();
            }
            return new EncShiftAlgorithm();
        } else {
            if ("unicode".equals(name)) {
                return new DescUnicodeAlgorithm();
            }
            return new DescShiftAlgorithm();
        }
    }
}

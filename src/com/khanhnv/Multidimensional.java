package com.khanhnv;

import java.util.Scanner;

public class Multidimensional {

    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        String[][] arr = new String[n][n];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (i == n / 2 || j == n / 2 || i == j || i == n - j - 1) {
                    arr[i][j] = "*";
                } else {
                    arr[i][j] = ".";
                }
            }
        }


        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println("");
        }
    }
}

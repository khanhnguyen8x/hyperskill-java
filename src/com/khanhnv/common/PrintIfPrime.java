package com.khanhnv.common;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrintIfPrime {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        while (scanner.hasNext()) {
            int number = scanner.nextInt();
            // create and submit tasks
            executor.submit(new PrintIfPrimeTask(number));
        }
        executor.shutdown();
    }
}


class PrintIfPrimeTask implements Runnable {
    private final int number;

    public PrintIfPrimeTask(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        // write code of task here
        if (isPrime(number)) {
            System.out.println(number);
        }
    }

    boolean isPrime(int n) {

        // Check if number is less than
        // equal to 1
        if (n <= 1)
            return false;

            // Check if number is 2
        else if (n == 2)
            return true;

            // Check if n is a multiple of 2
        else if (n % 2 == 0)
            return false;

        // If not, then just check the odds
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }
}
package com.khanhnv.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorDemo {

    private final static int POOL_SIZE = 2;
    private final static int NUMBER_OF_TASKS = 20;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            int taskNumber = i;
            executor.submit(() -> {
                var taskName = "task-" + taskNumber;
                var threadName = Thread.currentThread().getName();
                System.out.printf("%s executes %s\n", taskName, threadName);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

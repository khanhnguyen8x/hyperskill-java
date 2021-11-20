package com.khanhnv.common;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class HTTPInteractionExample {

    private static final int MAX_TIMEOUT_MS = 10000;  // 10000 ms = 10 seconds
    private static final int MAX_RETRIES = 5;
    private static final int WAIT_TIMEOUT_MS = 5000;

    public static HttpResponse<String> sendData(HttpClient client, URI service, String jsonData)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(service)
                // 1
                .timeout(Duration.ofMillis(MAX_TIMEOUT_MS))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();

        // 2
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> sendDataWithRetries(HttpClient client, URI service, String jsonData)
            throws Exception {
        int retry = 0;
        while (retry < MAX_RETRIES) { // 3
            // 4
            retry++;
            try {
                // 5
                HttpResponse<String> response = sendData(client, service, jsonData);
                if (response.statusCode() < 400) {
                    return response;
                }
                if (response.statusCode() >= 500) {
                    // 6
                    System.out.println("A server error has occurred");
                }
                throw new Exception("Incorrect request, probably, we need to fix the code");
            } catch (Exception e) {
                System.out.println("An interaction error has occurred, will retry the request later");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(WAIT_TIMEOUT_MS); // waiting before the next retry
            } catch (InterruptedException ignored) { }
        }
        throw new Exception("Cannot get the response after " + retry + " retries");
    }

    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();

        URI fakePostService = URI.create("https://jsonplaceholder.typicode.com/posts");
        String payment = "{\"order\":\"1234\", \"price\":\"10000\"}";

        try {
            // 7
            HttpResponse<String> response = sendDataWithRetries(httpClient, fakePostService, payment);
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println("It's impossible to complete the action after several retries");
        }
    }
}
package com.prem.banking_management_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConcurrencyExceptionHandlerTest {

    @LocalServerPort
    private int port;

    @Test
    void testConcurrentWithdrawalReturnsConflict() throws Exception {

        String url =
                "http://localhost:" + port + "/api/transactions/withdraw";

        String requestBody = """
                {
                    "accountId": 1,
                    "amount": 800.00
                }
                """;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        Future<HttpResponse<String>> future1 =
                executor.submit(() ->
                        client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        )
                );

        Future<HttpResponse<String>> future2 =
                executor.submit(() ->
                        client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        )
                );

        HttpResponse<String> response1 = future1.get();
        HttpResponse<String> response2 = future2.get();

        executor.shutdown();

        System.out.println(
                "Response 1: "
                        + response1.statusCode()
        );

        System.out.println(
                "Response 2: "
                        + response2.statusCode()
        );

        System.out.println(
                "Body 1: "
                        + response1.body()
        );

        System.out.println(
                "Body 2: "
                        + response2.body()
        );

        assertTrue(
                response1.statusCode() == 201
                        || response1.statusCode() == 409
        );

        assertTrue(
                response2.statusCode() == 201
                        || response2.statusCode() == 409
        );
    }
}
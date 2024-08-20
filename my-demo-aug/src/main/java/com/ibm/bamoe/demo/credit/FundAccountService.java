package com.ibm.bamoe.demo.credit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FundAccountService {

    private final HttpClient httpClient;

    public FundAccountService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String fund(String accountId, int amount) {
        // Validate input
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        try {
            boolean success = callExternalFundingSystem(accountId, amount);

            if (success) {
                String transactionId = generateTransactionId();
                return new FundingResult(true, transactionId, "Funding successful").getTransactionId();
            } else {
                return new FundingResult(false, null, "Funding failed").getTransactionId();
            }
        } catch (Exception e) {
            return new FundingResult(false, null, "Funding failed: " + e.getMessage()).getTransactionId();
        }
    }

    private boolean callExternalFundingSystem(String accountId, int amount) throws Exception {
        if (accountId != null ) return true;

        String jsonBody = String.format("{\"accountId\":\"%s\",\"amount\":%s}", accountId, amount);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.externalfundingsystem.com/fund"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        try {
            HttpResponse<String> result = response.get();
            return result.statusCode() == 200;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new Exception("Failed to call external funding system", e);
        }
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}
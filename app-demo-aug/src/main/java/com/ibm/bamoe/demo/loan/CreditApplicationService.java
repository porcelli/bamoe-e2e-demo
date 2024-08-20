package com.ibm.bamoe.demo.loan;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class CreditApplicationService {
    
    private static final Logger LOGGER = Logger.getLogger(CreditApplicationService.class);
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String apply(String fullName, int income, int score){
        try {
            // Prepare the payload with only Full Name and Income
            JsonObject externalPayload = new JsonObject()
            .put("name", fullName)
            .put("income", income / 12)
            .put("score", score);

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Config.REMOTE_URL + "/CreditApplication"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(externalPayload.toString()))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            // Check the status of the response
            if (response.statusCode() == 201) {
                VerificationResponse verificationResponse = objectMapper.readValue(response.body(), VerificationResponse.class);
                if ("approved".equalsIgnoreCase(verificationResponse.getStatus())) {
                    LOGGER.info("Loan application verified for " + fullName);
                    return "success";
                } else {
                    LOGGER.warn("Loan application not verified for " + fullName);
                    return "need verify";
                }
            } else {
                LOGGER.error("External service returned error: " + response.statusCode());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to process loan application", e);
        }

        return "error";
    }

}

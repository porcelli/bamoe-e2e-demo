package com.ibm.bamoe.demo.credit;

import java.util.Random;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateAccountService {

    private static final int ACCOUNT_NUMBER_LENGTH = 8;
    private static final String ACCOUNT_NUMBER_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random = new Random();

    public String createAccount(String clientName) {
        // Validate input
        if (clientName == null || clientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be null or empty");
        }

        // Generate account number
        return generateAccountNumber();
    }

    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder(ACCOUNT_NUMBER_LENGTH);
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            int randomIndex = random.nextInt(ACCOUNT_NUMBER_CHARS.length());
            sb.append(ACCOUNT_NUMBER_CHARS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
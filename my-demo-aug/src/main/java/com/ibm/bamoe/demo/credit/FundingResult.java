package com.ibm.bamoe.demo.credit;

public class FundingResult {
    private final boolean success;
    private final String transactionId;
    private final String message;

    public FundingResult(boolean success, String transactionId, String message) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FundingResult{" +
                "success=" + success +
                ", transactionId='" + transactionId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
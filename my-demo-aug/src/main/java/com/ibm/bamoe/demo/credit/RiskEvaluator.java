package com.ibm.bamoe.demo.credit;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RiskEvaluator {
    
    public static final int MIN_CREDIT_SCORE = 300;
    public static final int MAX_CREDIT_SCORE = 850;
    public static final double MIN_MONTHLY_INCOME = 0;
    
    public String evaluate(int creditScore, int monthlyIncome) {
        if (creditScore < MIN_CREDIT_SCORE || creditScore > MAX_CREDIT_SCORE) {
            throw new IllegalArgumentException("Credit score must be between 300 and 850");
        }
        if (monthlyIncome < MIN_MONTHLY_INCOME) {
            throw new IllegalArgumentException("Monthly income cannot be negative");
        }
        
        double riskScore = calculateRiskScore(creditScore, monthlyIncome);
        return new RiskAssessment(riskScore, determineRiskCategory(riskScore)).getRiskCategory().toString().toLowerCase();
    }
    
    private double calculateRiskScore(int creditScore, int monthlyIncome) {
        // Normalize credit score to a 0-1 scale
        double normalizedCreditScore = (creditScore - MIN_CREDIT_SCORE) / (double)(MAX_CREDIT_SCORE - MIN_CREDIT_SCORE);
        
        // Calculate income factor (assumes higher income is lower risk)
        double incomeFactor = Math.min(1, Math.log(monthlyIncome / 1000) / Math.log(10));
        
        // Weighted combination of credit score and income factor
        return (normalizedCreditScore * 0.7) + (incomeFactor * 0.3);
    }
    
    private RiskCategory determineRiskCategory(double riskScore) {
        if (riskScore >= 0.8) return RiskCategory.LOW;
        if (riskScore >= 0.6) return RiskCategory.MODERATE;
        if (riskScore >= 0.4) return RiskCategory.HIGH;
        return RiskCategory.VERY_HIGH;
    }
    
   
    public static class RiskAssessment {
        private final double riskScore;
        private final RiskCategory riskCategory;
        
        public RiskAssessment(double riskScore, RiskCategory riskCategory) {
            this.riskScore = riskScore;
            this.riskCategory = riskCategory;
        }
        
        public double getRiskScore() {
            return riskScore;
        }
        
        public RiskCategory getRiskCategory() {
            return riskCategory;
        }
        
        @Override
        public String toString() {
            return String.format("Risk Score: %.2f, Risk Category: %s", riskScore, riskCategory);
        }
    }

    public static void main(String... args){
        RiskEvaluator evaluator = new RiskEvaluator();
        System.out.println(evaluator.evaluate(600, 6000)); //moderate
        System.out.println(evaluator.evaluate(700, 4000)); //moderate
        System.out.println(evaluator.evaluate(800, 3000)); //moderate
    }
}
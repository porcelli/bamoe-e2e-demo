package com.ibm.bamoe.demo.loan;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreditScoreService {
    
    public int getScore(String ssn){

        try {
            return Integer.valueOf( ssn.substring(0, 3));
        } catch (Exception ex) {
        }

        return 650;
    }

}
